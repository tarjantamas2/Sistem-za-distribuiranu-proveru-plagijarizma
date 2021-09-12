package rs.ac.uns.ftn.frontend.views.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceTaskDto;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.SharedTaskDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedTaskViewModel {

  private String id;

  private String candidateDocumentId;

  private String candidateDocumentMd5;

  private String status;

  private String ownerId;

  public static SharedTaskViewModel fromSharedTask(SharedTaskDto sharedTaskDto) {
    String candidateMd5 = sharedTaskDto.getServiceTasks().get(0).getCandidateDocumentMd5();
    long notCompleted = sharedTaskDto.getServiceTasks().stream().filter(ServiceTaskDto::isNotFinished).count();
    String status = String.format("%s out of %s completed", sharedTaskDto.getServiceTasks().size() - notCompleted,
        sharedTaskDto.getServiceTasks().size());
    String candidateDocumentId = sharedTaskDto.getServiceTasks().get(0).getCandidateDocumentId();

    return new SharedTaskViewModel(sharedTaskDto.getId(), candidateDocumentId, candidateMd5, status,
        sharedTaskDto.getOwnerId());
  }
}
