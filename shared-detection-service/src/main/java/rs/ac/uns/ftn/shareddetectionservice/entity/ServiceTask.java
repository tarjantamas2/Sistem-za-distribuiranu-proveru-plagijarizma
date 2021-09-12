package rs.ac.uns.ftn.shareddetectionservice.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResult;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceTask {

  private String id;

  private String serviceId;

  private String candidateDocumentId;

  private String candidateDocumentMd5;

  private TaskStatus status;

  private String statusDescription;

  private String algorithmId;

  private List<TaskResult> results;

  public boolean isNotFinished() {
    return status == TaskStatus.RUNNING || status == TaskStatus.PENDING;
  }
}
