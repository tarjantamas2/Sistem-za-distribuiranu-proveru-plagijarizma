package rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.util.TempFileUtil;
import rs.ac.uns.ftn.springcommonrestexceptions.exception.BadRequestException;

@Slf4j
public class DetectionRestClient {

  private final RestTemplate restTemplate;

  private final TempFileUtil tempFileUtil;

  private final String tasksEndpoint;

  private final String taskEndpoint;

  private final String algorithmsEndpoint;

  public DetectionRestClient(RestTemplate restTemplate, TempFileUtil tempFileUtil, String tasksEndpoint,
      String taskEndpoint, String algorithmsEndpoint) {
    this.restTemplate = restTemplate;
    this.tempFileUtil = tempFileUtil;
    this.tasksEndpoint = tasksEndpoint;
    this.taskEndpoint = taskEndpoint;
    this.algorithmsEndpoint = algorithmsEndpoint;
  }

  public ResponseEntity<CreateTaskResponse> createTask(CreateTaskRequest createTaskRequest) {
    return createTask(createTaskRequest, false);
  }

  public ResponseEntity<CreateTaskResponse> createTask(CreateTaskRequest createTaskRequest, boolean simulate) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    HttpEntity<Object> requestEntity = new HttpEntity<>(toFormData(createTaskRequest), headers);
    return restTemplate.postForEntity(tasksEndpoint + getSimulateParam(simulate), requestEntity,
        CreateTaskResponse.class, Collections.emptyMap());
  }

  public ResponseEntity<CreateTaskResponse> createTask(CreateTaskRelayedRequest createTaskRelayedRequest) {
    return createTask(createTaskRelayedRequest, false);
  }

  public ResponseEntity<CreateTaskResponse> createTask(CreateTaskRelayedRequest createTaskRelayedRequest,
      boolean simulate) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    return tempFileUtil.withTempFile(tempFileName -> {
      HttpEntity<Object> requestEntity = new HttpEntity<>(toFormData(createTaskRelayedRequest, tempFileName), headers);
      return restTemplate.postForEntity(tasksEndpoint + getSimulateParam(simulate), requestEntity,
          CreateTaskResponse.class, Collections.emptyMap());
    });
  }

  public ResponseEntity<FindTaskByIdResponse> findTaskById(String id) {
    return findTaskById(id, false);
  }

  public ResponseEntity<FindTaskByIdResponse> findTaskById(String id, boolean simulate) {
    return restTemplate.getForEntity(taskEndpoint + "/" + id + getSimulateParam(simulate), FindTaskByIdResponse.class,
        Collections.emptyMap());
  }

  public ResponseEntity<FindAllTasksResponse> findAllTasks() {
    return findAllTasks(false);
  }

  public ResponseEntity<FindAllTasksResponse> findAllTasks(boolean simulate) {
    return restTemplate.getForEntity(tasksEndpoint + getSimulateParam(simulate), FindAllTasksResponse.class,
        Collections.emptyMap());
  }

  public ResponseEntity<FindAllAlgorithmsResponse> findAllAlgorithms() {
    return findAllAlgorithms(false);
  }

  public ResponseEntity<FindAllAlgorithmsResponse> findAllAlgorithms(boolean simulate) {
    return restTemplate.getForEntity(algorithmsEndpoint + getSimulateParam(simulate), FindAllAlgorithmsResponse.class,
        Collections.emptyMap());
  }

  private String getSimulateParam(boolean simulate) {
    return getParam("simulate", simulate);
  }

  private String getParam(String name, Object value) {
    return "?" + name + "=" + value;
  }

  private MultiValueMap<String, Object> toFormData(CreateTaskRelayedRequest createTaskRelayedRequest,
      String tempFileName) {
    MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
    try {
      Files.copy(createTaskRelayedRequest.getFile().getInputStream(), Paths.get(tempFileName));
    } catch (Exception e) {
      e.printStackTrace();
    }

    formData.add("file", new FileSystemResource(tempFileName));
    String name = createTaskRelayedRequest.getName();
    formData.add("name", name == null ? createTaskRelayedRequest.getFile().getName() : name);
    formData.add("algorithmId", createTaskRelayedRequest.getAlgorithmId());
    return formData;
  }

  private MultiValueMap<String, Object> toFormData(CreateTaskRequest createTaskRequest) {
    MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();

    formData.add("file", new FileSystemResource(createTaskRequest.getFile()));
    String name = createTaskRequest.getName();
    formData.add("name", name == null ? createTaskRequest.getFile().getName() : name);
    formData.add("algorithmId", createTaskRequest.getAlgorithmId());
    return formData;
  }

  public void testRun() {
    try {
      tempFileUtil.withTempFile(tempName -> {
        createDummyFileForTestRun(tempName);
        verifyFieldsAreNotNull(
            createTask(new CreateTaskRequest(new File(tempName), tempName, "algId"), true).getBody());
        return null;
      });
      verifyFieldsAreNotNull(findAllAlgorithms(true));
      verifyFieldsAreNotNull(findAllTasks(true));
      verifyFieldsAreNotNull(findTaskById("test", true));
    } catch (Exception e) {
      throw new BadRequestException("Unable to verify service protocol compliance.");
    }
  }

  private void verifyFieldsAreNotNull(Object object) {
    if (object == null) {
      throw new BadRequestException("Service is not compliant to the protocol.");
    }

    if (object instanceof Collection) {
      for (Object o : ((Collection<?>) object)) {
        if (o.getClass().equals(Object.class)) {
          throw new BadRequestException("Service is not compliant with the protocol.");
        }
        verifyFieldsAreNotNull(o);
      }
      return;
    }

    if (!object.getClass().getPackage().getName().contains("rs.ac.uns.ftn")) {
      return;
    }

    if (object.getClass().isEnum()) {
      return;
    }

    Class<?> aClass = object.getClass();
    if (aClass.equals(Object.class)) {
      return;
    }

    for (Field field : aClass.getDeclaredFields()) {
      try {
        field.setAccessible(true);
        Object o2 = field.get(object);
        field.setAccessible(false);
        verifyFieldsAreNotNull(o2);
      } catch (IllegalAccessException e) {
        log.error("Unable to access field.", e);
        throw new BadRequestException("Service is not compliant to the protocol.");
      }
    }
  }

  private void createDummyFileForTestRun(String tempName) {
    try {
      FileUtils.writeStringToFile(new File(tempName), "Test", Charset.defaultCharset());
    } catch (Exception e) {
      throw new BadRequestException("Unable to verify service protocol compliance.");
    }
  }

}
