package rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SharedRestClient {

  @Value("${ftn.shared.login}")
  public String loginEndpoint;

  @Value("${ftn.shared.user}")
  public String userEndpoint;

  @Value("${ftn.shared.users}")
  public String usersEndpoint;

  @Value("${ftn.shared.task}")
  public String taskEndpoint;

  @Value("${ftn.shared.tasks}")
  public String tasksEndpoint;

  @Value("${ftn.shared.services}")
  public String servicesEndpoint;

  private final RestTemplate restTemplate;

  @Nullable
  private final TokenPersistenceHandler tokenPersistenceHandler;

  public List<ServiceMetadataWithAlgorithms> findAllServices() {
    try {
      ResponseEntity<List<ServiceMetadataWithAlgorithms>> responseEntity =
          restTemplate.exchange(servicesEndpoint, HttpMethod.GET, new HttpEntity<>(getAuthHeader()),
              new ParameterizedTypeReference<List<ServiceMetadataWithAlgorithms>>() {
              });
      List<ServiceMetadataWithAlgorithms> services = responseEntity.getBody();
      if (services == null) {
        return Collections.emptyList();
      }
      return services;
    } catch (Exception e) {
      log.error("Could not get services:", e);
      return Collections.emptyList();
    }
  }

  public List<SharedTaskDto> findAllSharedTasks() {
    try {
      ResponseEntity<List<SharedTaskDto>> responseEntity = restTemplate.exchange(tasksEndpoint, HttpMethod.GET,
          new HttpEntity<>(getAuthHeader()), new ParameterizedTypeReference<List<SharedTaskDto>>() {
          });
      List<SharedTaskDto> tasks = responseEntity.getBody();
      if (tasks == null) {
        return Collections.emptyList();
      }
      return tasks;
    } catch (Exception e) {
      log.error("Could not get tasks:", e);
      return Collections.emptyList();
    }
  }

  public boolean login(UserDto userDto) {
    try {
      TokenResponse tokenResponse = restTemplate.postForEntity(loginEndpoint, userDto, TokenResponse.class).getBody();
      if (tokenResponse == null) {
        return false;
      }
      saveToken(tokenResponse.getToken());
      if (tokenPersistenceHandler.getToken() != null) {
        return true;
      }
    } catch (Exception e) {
      log.error("Could not login: ", e);
    }
    return false;
  }

  public Optional<UserDto> register(UserDto userDto) {
    try {
      UserDto response = restTemplate.postForEntity(usersEndpoint, userDto, UserDto.class).getBody();
      return Optional.ofNullable(response);
    } catch (Exception e) {
      log.error("Could not login: ", e);
      return Optional.empty();
    }
  }

  public Optional<UserDto> getLoggedInUser() {
    if (tokenPersistenceHandler == null) {
      return Optional.empty();
    }

    if (tokenPersistenceHandler.getToken() == null) {
      return Optional.empty();
    }

    try {
      ResponseEntity<UserDto> response =
          restTemplate.exchange(userEndpoint + "/me", HttpMethod.GET, new HttpEntity<>(getAuthHeader()), UserDto.class);
      return Optional.ofNullable(response.getBody());
    } catch (Exception e) {
      log.error("Exception: ", e);
      return Optional.empty();
    }
  }

  public void logout() {
    saveToken(null);
  }

  public HttpHeaders getAuthHeader() {
    if (tokenPersistenceHandler == null) {
      throw new RuntimeException("Token persistence handler should not be null.");
    }
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + tokenPersistenceHandler.getToken());
    return headers;
  }

  private void saveToken(String token) {
    if (tokenPersistenceHandler != null) {
      tokenPersistenceHandler.saveToken(token);
    }
  }

  public Optional<SharedTaskDto> createTask(File file, String name, List<ServiceTaskDto> selectedServices) {
    if (StringUtils.isBlank(name)) {
      name = file.getName();
    }
    try {
      HttpHeaders serviceTaskHeaders = new HttpHeaders();
      serviceTaskHeaders.add("Content-Type", "application/json");
      HttpEntity<List<ServiceTaskDto>> serviceTasksEntity = new HttpEntity<>(selectedServices, serviceTaskHeaders);

      MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
      formData.add("file", new FileSystemResource(file));
      formData.add("name", name);
      formData.add("serviceTasks", serviceTasksEntity);

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
      headers.addAll(getAuthHeader());

      HttpEntity<Object> requestEntity = new HttpEntity<>(formData, headers);
      ResponseEntity<SharedTaskDto> responseEntity =
          restTemplate.postForEntity(tasksEndpoint, requestEntity, SharedTaskDto.class);

      return Optional.ofNullable(responseEntity.getBody());
    } catch (Exception e) {
      log.error("Could not create task: ", e);
      return Optional.empty();
    }
  }

  public Optional<SharedTaskDto> findTaskById(String taskId) {
    try {
      ResponseEntity<SharedTaskDto> responseEntity = restTemplate.exchange(taskEndpoint + "/" + taskId, HttpMethod.GET,
          new HttpEntity<>(getAuthHeader()), SharedTaskDto.class);
      return Optional.ofNullable(responseEntity.getBody());
    } catch (Exception e) {
      log.error("Could not find task by id: ", e);
      return Optional.empty();
    }
  }
}
