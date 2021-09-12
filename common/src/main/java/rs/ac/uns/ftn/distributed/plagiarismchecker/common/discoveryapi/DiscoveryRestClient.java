package rs.ac.uns.ftn.distributed.plagiarismchecker.common.discoveryapi;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.ServiceMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.util.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscoveryRestClient {

  @Value("${ftn.discovery.services-endpoint}")
  public String servicesEndpoint;

  @Value("${ftn.discovery.service-endpoint}")
  public String serviceEndpoint;

  private final RestClient restClient;

  public ServiceMetadata findById(String serviceId) {
    return restClient.get(serviceEndpoint + "/" + serviceId, ServiceMetadata.class);
  }

  public List<ServiceMetadata> findAll() {
    List<ServiceMetadata> serviceMetadataList =
        restClient.get(servicesEndpoint, new ParameterizedTypeReference<List<ServiceMetadata>>() {
        });

    return serviceMetadataList == null ? Collections.emptyList() : serviceMetadataList;
  }

  public ServiceMetadata register(int port, String description) {
    return register(Integer.toString(port), description);
  }

  public ServiceMetadata register(String port, String description) {
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    int tries = 0;

    while (tries < 3) {
      tries++;

      try {
        ResponseEntity<ServiceMetadata> response = new RestTemplate().exchange(servicesEndpoint, HttpMethod.POST,
            new HttpEntity<>(new ServiceMetadata(null, null, port, description)), ServiceMetadata.class,
            Collections.emptyMap());

        if (response.getStatusCode() == HttpStatus.OK) {
          return response.getBody();
        }

        log.error("Unable to register on discovery service. Retrying in 5 seconds...");
      } catch (Exception e) {
        log.error("Unable to register on discovery service. Retrying in 5 seconds...", e);
      }

      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        log.error("Error occurred while busy waiting: ", e);
      }
    }

    log.error("Unable to register on discovery service. Giving up.");
    return null;
  }
}
