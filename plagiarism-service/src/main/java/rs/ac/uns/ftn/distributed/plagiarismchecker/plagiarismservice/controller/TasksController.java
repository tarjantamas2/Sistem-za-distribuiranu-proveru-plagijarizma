package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.CreateTaskRelayedRequest;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.CreateTaskResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.FindAllTasksResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.service.TaskService;
import uk.co.jemos.podam.api.PodamFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tasks")
public class TasksController {

  private final TaskService taskService;

  private final PodamFactory podamFactory;

  @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<CreateTaskResponse> create(@ModelAttribute @Valid CreateTaskRelayedRequest task,
      @RequestParam(required = false, defaultValue = "false") boolean simulate) {
    if (simulate) {
      return ResponseEntity.ok(podamFactory.manufacturePojo(CreateTaskResponse.class));
    }
    return ResponseEntity.ok(taskService.create(task));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<FindAllTasksResponse> findAll(
      @RequestParam(required = false, defaultValue = "false") boolean simulate) {
    if (simulate) {
      return ResponseEntity.ok(podamFactory.manufacturePojo(FindAllTasksResponse.class));
    }
    return ResponseEntity.ok(taskService.findAll());
  }
}
