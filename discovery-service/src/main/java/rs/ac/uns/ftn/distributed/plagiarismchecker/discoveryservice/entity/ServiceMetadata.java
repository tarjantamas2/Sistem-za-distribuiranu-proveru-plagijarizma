package rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = "service-metadata")
public class ServiceMetadata {

  @Id
  private String id;

  private String address;

  private String port;

  private String description;

  public String resolveEndpoint(String endpoint) {
    return "http://" + address + ":" + port + endpoint;
  }

}
