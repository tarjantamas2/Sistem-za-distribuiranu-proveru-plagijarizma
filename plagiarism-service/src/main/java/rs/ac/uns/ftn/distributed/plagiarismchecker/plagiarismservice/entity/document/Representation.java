package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Representation {

  private String mimeType;

  private String fileName;
}
