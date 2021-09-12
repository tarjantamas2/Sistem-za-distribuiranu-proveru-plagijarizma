package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.task;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResult;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "task")
public class Task {

  @Id
  private String id;

  private String candidateDocumentId;

  private String candidateDocumentMd5;

  private TaskStatus status;

  private String statusDescription;

  private String algorithmId;

  private List<TaskResult> results;
}
