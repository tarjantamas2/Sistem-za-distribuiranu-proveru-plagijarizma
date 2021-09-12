package rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.entity.ServiceMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.service.ServiceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/service/{id}")
public class ServiceController {

  private final ServiceService serviceService;

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ServiceMetadata> findById(@PathVariable String id) {
    return ResponseEntity.ok(serviceService.findById(id));
  }

  @PutMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ServiceMetadata> update(@PathVariable String id, HttpServletRequest servletRequest,
      @RequestBody ServiceMetadata serviceMetadata) {
    return ResponseEntity.ok(serviceService.update(id, serviceMetadata, servletRequest.getRemoteAddr()));
  }
}
