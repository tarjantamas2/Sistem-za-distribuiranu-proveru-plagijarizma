package rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi;

import java.io.File;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTaskRequest {

  @NotNull
  private File file;

  private String name;

  @NotBlank
  private String algorithmId;
}
