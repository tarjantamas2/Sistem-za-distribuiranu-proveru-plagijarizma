package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.DocumentMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.dto.DocumentUpload;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.service.ReferenceDocumentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reference-documents")
public class ReferenceDocumentsController {

  private final ReferenceDocumentService referenceDocumentService;

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DocumentMetadata>> uploadReferenceDocument(@ModelAttribute DocumentUpload documentUpload) {
    return ResponseEntity.ok(referenceDocumentService.create(documentUpload));
  }
}
