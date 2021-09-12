package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document;

import java.util.ArrayList;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "candidate-documents")
public class CandidateDocument extends DocumentMetadata {

  public CandidateDocument(String id, String mimeType, String md5, String fileName, String extension) {
    super(id, mimeType, md5, fileName, extension, new ArrayList<>());
  }
}
