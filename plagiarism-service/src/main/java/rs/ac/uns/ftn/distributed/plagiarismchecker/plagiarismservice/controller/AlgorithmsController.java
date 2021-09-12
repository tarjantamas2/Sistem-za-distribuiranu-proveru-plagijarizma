package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.FindAllAlgorithmsResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.service.AlgorithmsService;
import uk.co.jemos.podam.api.PodamFactory;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/algorithms")
public class AlgorithmsController {

  private final AlgorithmsService algorithmsService;

  private final PodamFactory podamFactory;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<FindAllAlgorithmsResponse> findAll(
      @RequestParam(required = false, defaultValue = "false") boolean simulate) {
    if (simulate) {
      return ResponseEntity.ok(podamFactory.manufacturePojo(FindAllAlgorithmsResponse.class));
    }
    return ResponseEntity.ok(algorithmsService.findAll());
  }
}
