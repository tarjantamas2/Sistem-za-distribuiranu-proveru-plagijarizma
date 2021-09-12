package rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmResponse {

  private String id;

  private String name;

  private String description;

  private List<String> supportedMimeTypes;
}
