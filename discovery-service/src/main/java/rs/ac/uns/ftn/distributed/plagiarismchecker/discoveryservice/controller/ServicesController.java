package rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.entity.ServiceMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.service.ServiceService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class ServicesController {

  private final ServiceService serviceService;

  @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<ServiceMetadata> register(HttpServletRequest servletRequest,
      @RequestBody ServiceMetadata serviceMetadata) {
    return ResponseEntity.ok(serviceService.register(serviceMetadata, servletRequest.getRemoteAddr()));
  }

  @GetMapping(produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<ServiceMetadata>> findAll() {
    return ResponseEntity.ok(serviceService.findAll());
  }
}
