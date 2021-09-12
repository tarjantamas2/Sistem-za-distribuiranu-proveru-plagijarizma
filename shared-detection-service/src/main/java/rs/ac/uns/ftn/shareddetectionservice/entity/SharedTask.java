package rs.ac.uns.ftn.shareddetectionservice.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "shared-tasks")
public class SharedTask {

  @Id
  private String id;

  private String candidateDocumentId;

  private String candidateDocumentMd5;

  private List<ServiceTask> serviceTasks;

  private String ownerId;
}
