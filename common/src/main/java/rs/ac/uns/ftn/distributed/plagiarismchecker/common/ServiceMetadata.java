package rs.ac.uns.ftn.distributed.plagiarismchecker.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceMetadata {

  private String id;

  private String address;

  private String port;

  private String description;

  public String resolveEndpoint(String endpoint) {
    return "http://" + address + ":" + port + endpoint;
  }
}
