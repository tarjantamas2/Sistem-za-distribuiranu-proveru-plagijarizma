package rs.ac.uns.ftn.distributed.plagiarismchecker.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResult {

  private String referenceDocumentId;

  private String referenceDocumentMd5;

  private Double score;

  private List<TaskResultPart> parts;
}
