package rs.ac.uns.ftn.shareddetectionservice.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskStatus;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.*;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedTaskDto;
import rs.ac.uns.ftn.shareddetectionservice.entity.ServiceTask;
import rs.ac.uns.ftn.shareddetectionservice.entity.SharedTask;
import rs.ac.uns.ftn.shareddetectionservice.repository.SharedTaskRepository;
import rs.ac.uns.ftn.springauthcommon.service.UserService;
import rs.ac.uns.ftn.springcommonrestexceptions.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class SharedTaskService {

  private final SharedTaskRepository sharedTaskRepository;

  private final DetectionRestClientFactory detectionRestClientFactory;

  private final UserService userService;

  private final ModelMapper modelMapper;

  @PostConstruct
  public void postConstruct() {
    // sharedTaskRepository.deleteAll();
  }

  public SharedTask create(SharedTaskDto sharedTaskDto) {
    SharedTask sharedTask = modelMapper.map(sharedTaskDto, SharedTask.class);
    sharedTask.setOwnerId(userService.getActiveUserOrFail().getId());

    for (ServiceTask serviceTask : sharedTask.getServiceTasks()) {
      createServiceTask(serviceTask, sharedTaskDto);
    }
    return sharedTaskRepository.save(sharedTask);
  }

  private void createServiceTask(ServiceTask serviceTask, SharedTaskDto sharedTaskDto) {
    DetectionRestClient restClient = detectionRestClientFactory.getInstance(serviceTask.getServiceId());

    CreateTaskRelayedRequest createTaskRelayedRequest =
        new CreateTaskRelayedRequest(sharedTaskDto.getFile(), sharedTaskDto.getName(), serviceTask.getAlgorithmId());

    CreateTaskResponse task = restClient.createTask(createTaskRelayedRequest).getBody();
    if (task == null) {
      return;
    }

    modelMapper.map(task, serviceTask);
  }

  public SharedTask findById(String id) {
    SharedTask sharedTask =
        sharedTaskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task with given id not found."));
    for (ServiceTask serviceTask : sharedTask.getServiceTasks()) {
      if (serviceTask.isNotFinished()) {
        sharedTask.getServiceTasks().forEach(this::updateTask);
        sharedTaskRepository.save(sharedTask);
      }
    }

    return sharedTask;
  }

  public List<SharedTask> findAllForCurrentUser() {
    List<SharedTask> sharedTasks = sharedTaskRepository.findByOwnerId(userService.getActiveUserOrFail().getId());
    sharedTasks.forEach(task -> task.getServiceTasks().forEach(this::updateTask));
    sharedTaskRepository.saveAll(sharedTasks);

    return sharedTasks;
  }

  private void updateTask(ServiceTask serviceTask) {
    if (serviceTask.getStatus() != TaskStatus.PENDING && serviceTask.getStatus() != TaskStatus.RUNNING) {
      return;
    }
    DetectionRestClient restClient = detectionRestClientFactory.getInstance(serviceTask.getServiceId());
    try {
      FindTaskByIdResponse task = restClient.findTaskById(serviceTask.getId()).getBody();
      if (task == null) {
        return;
      }
      modelMapper.map(task, serviceTask);
    } catch (Exception e) {
    }
  }
}
