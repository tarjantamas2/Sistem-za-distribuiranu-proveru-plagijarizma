package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class DocumentMetadata {

  @Id
  private String id;

  private String mimeType;

  private String md5;

  private String fileName;

  private String extension;

  private List<Representation> representations = new ArrayList<>();

  public DocumentMetadata(String id, String mimeType, String md5, String fileName, String extension,
      List<Representation> representations) {
    this.id = id;
    this.mimeType = mimeType;
    this.md5 = md5;
    this.fileName = fileName;
    this.extension = extension;
    this.representations = representations;
  }

  public void addRepresentation(Representation representation) {
    representations.add(representation);
  }

  public Representation getRepresentation(String mimeType) {
    return representations.stream().filter(r -> r.getMimeType().equals(mimeType)).findFirst().orElse(null);
  }
}
