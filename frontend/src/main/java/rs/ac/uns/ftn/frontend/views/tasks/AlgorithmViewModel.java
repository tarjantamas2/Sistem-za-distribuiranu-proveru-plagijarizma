package rs.ac.uns.ftn.frontend.views.tasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.Algorithm;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlgorithmViewModel {

  private String id;

  private String name;

  private String description;

  private String supportedMimeTypes;

  public static AlgorithmViewModel fromDto(Algorithm algorithm) {
    return new AlgorithmViewModel(algorithm.getId(), algorithm.getName(), algorithm.getDescription(),
        String.join(", ", algorithm.getSupportedMimeTypes()));
  }
}
