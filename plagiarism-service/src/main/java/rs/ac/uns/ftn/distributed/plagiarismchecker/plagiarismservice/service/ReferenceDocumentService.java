package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.DocumentMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.dto.DocumentUpload;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.ReferenceDocument;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest.BadRequestException;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.DocumentRepository;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.ReferenceDocumentRepository;

@Service
@RequiredArgsConstructor
public class ReferenceDocumentService {

  private final ReferenceDocumentRepository referenceDocumentRepository;

  private final DocumentRepository documentRepository;

  private final ModelMapper modelMapper;

  @PostConstruct
  public void postConstruct() {
    // referenceDocumentRepository.deleteAll();
  }

  public List<DocumentMetadata> create(DocumentUpload documentUpload) {
    if (ArrayUtils.isEmpty(documentUpload.getFiles())) {
      throw new BadRequestException("At least one reference file must be provided.");
    }
    for (MultipartFile file : documentUpload.getFiles()) {
      if (file.getSize() == 0L) {
        throw new BadRequestException("Zero byte files cannot be provided.");
      }
    }
    return Arrays.stream(documentUpload.getFiles()).map(file -> {
      ReferenceDocument referenceDocument = documentRepository.createReferenceDocument(file);
      if (StringUtils.isNotBlank(documentUpload.getName())) {
        referenceDocument.setFileName(documentUpload.getName());
      }
      return referenceDocumentRepository.save(referenceDocument);
    }).map(document -> modelMapper.map(document, DocumentMetadata.class)).collect(Collectors.toList());
  }
}
