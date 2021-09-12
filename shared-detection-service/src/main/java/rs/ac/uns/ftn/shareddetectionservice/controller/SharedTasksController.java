package rs.ac.uns.ftn.shareddetectionservice.controller;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceTaskDto;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedTaskDto;
import rs.ac.uns.ftn.shareddetectionservice.entity.SharedTask;
import rs.ac.uns.ftn.shareddetectionservice.service.SharedTaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shared-tasks")
public class SharedTasksController {

  private final SharedTaskService sharedTaskService;

  @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<SharedTask> create(@RequestPart @Valid @NotNull @NotBlank MultipartFile file,
      @RequestPart(required = false) String name, @RequestPart @Valid List<ServiceTaskDto> serviceTasks) {
    return ResponseEntity.ok(sharedTaskService.create(new SharedTaskDto(file, name, serviceTasks)));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<SharedTask>> findAllForCurrentUser() {
    return ResponseEntity.ok(sharedTaskService.findAllForCurrentUser());
  }
}
