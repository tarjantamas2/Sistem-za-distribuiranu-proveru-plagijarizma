package rs.ac.uns.ftn.distributed.plagiarismchecker.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentMetadata {

  private String id;

  private String mimeType;

  private String md5;

  private String fileName;

  private String extension;

  private List<Representation> representations;
}
