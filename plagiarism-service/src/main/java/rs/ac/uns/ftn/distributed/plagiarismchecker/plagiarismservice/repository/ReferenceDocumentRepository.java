package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.DocumentMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.ReferenceDocument;

public interface ReferenceDocumentRepository extends MongoRepository<ReferenceDocument, String> {

  List<DocumentMetadata> findByRepresentationsMimeType(String mimeType);
}
