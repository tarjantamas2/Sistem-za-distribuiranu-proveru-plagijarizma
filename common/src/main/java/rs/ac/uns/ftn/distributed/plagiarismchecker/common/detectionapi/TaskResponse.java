package rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResult;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {

  private String id;

  private TaskStatus status;

  private String statusDescription;

  private String algorithmId;

  private String candidateDocumentId;

  private String candidateDocumentMd5;

  private List<TaskResult> results;
}
