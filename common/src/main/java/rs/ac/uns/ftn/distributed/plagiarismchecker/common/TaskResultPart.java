package rs.ac.uns.ftn.distributed.plagiarismchecker.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResultPart {

  private String referencePartMd5;

  private String candidatePartMd5;
}
