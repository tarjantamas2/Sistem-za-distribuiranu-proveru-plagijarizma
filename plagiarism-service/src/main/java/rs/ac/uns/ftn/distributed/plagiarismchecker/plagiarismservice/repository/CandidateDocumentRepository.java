package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.CandidateDocument;

public interface CandidateDocumentRepository extends MongoRepository<CandidateDocument, String> {
}
