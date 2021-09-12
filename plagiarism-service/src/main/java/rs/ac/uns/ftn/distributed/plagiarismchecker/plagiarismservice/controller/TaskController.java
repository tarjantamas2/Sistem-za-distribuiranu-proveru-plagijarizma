package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.FindTaskByIdResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.service.TaskService;
import uk.co.jemos.podam.api.PodamFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/task/{id}")
public class TaskController {

  private final TaskService taskService;

  private final PodamFactory podamFactory;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FindTaskByIdResponse> finById(@PathVariable String id,
      @RequestParam(required = false, defaultValue = "false") boolean simulate) {
    if (simulate) {
      return ResponseEntity.ok(podamFactory.manufacturePojo(FindTaskByIdResponse.class));
    }
    return ResponseEntity.ok(taskService.findById(id));
  }
}
