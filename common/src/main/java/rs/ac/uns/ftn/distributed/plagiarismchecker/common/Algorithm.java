package rs.ac.uns.ftn.distributed.plagiarismchecker.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Algorithm {

  private String id;

  private String name;

  private String description;

  private List<String> supportedMimeTypes;

  public boolean isSupportedMimeType(String mimeType) {
    return supportedMimeTypes.contains(mimeType);
  }
}
