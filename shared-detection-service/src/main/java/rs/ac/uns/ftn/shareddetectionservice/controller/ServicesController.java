package rs.ac.uns.ftn.shareddetectionservice.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceMetadataWithAlgorithms;
import rs.ac.uns.ftn.shareddetectionservice.service.ServiceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class ServicesController {

  private final ServiceService serviceService;

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ServiceMetadataWithAlgorithms>> findAll() {
    return ResponseEntity.ok(serviceService.findAll());
  }
}
