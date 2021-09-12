package rs.ac.uns.ftn.distributed.plagiarismchecker.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

  private String id;

  private String candidateDocumentMd5;

  private TaskStatus status;

  private String statusDescription;

  private String algorithmId;

  private List<TaskResult> results;
}
