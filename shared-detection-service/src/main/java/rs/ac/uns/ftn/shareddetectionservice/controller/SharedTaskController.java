package rs.ac.uns.ftn.shareddetectionservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.shareddetectionservice.entity.SharedTask;
import rs.ac.uns.ftn.shareddetectionservice.service.SharedTaskService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shared-task/{id}")
public class SharedTaskController {

  private final SharedTaskService sharedTaskService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<SharedTask> finById(@PathVariable String id) {
    return ResponseEntity.ok(sharedTaskService.findById(id));
  }
}
