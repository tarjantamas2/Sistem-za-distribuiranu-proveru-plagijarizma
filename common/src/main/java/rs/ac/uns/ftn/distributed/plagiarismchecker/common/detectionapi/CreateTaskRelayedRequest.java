package rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRelayedRequest {

  @NotNull
  private MultipartFile file;

  private String name;

  @NotBlank
  private String algorithmId;
}
