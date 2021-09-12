package rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedTaskDto {

  private String id;

  private String ownerId;

  private MultipartFile file;

  private String name;

  private List<ServiceTaskDto> serviceTasks;

  public SharedTaskDto(MultipartFile file, String name, List<ServiceTaskDto> serviceTasks) {
    this.file = file;
    this.name = name;
    this.serviceTasks = serviceTasks;
  }
}
