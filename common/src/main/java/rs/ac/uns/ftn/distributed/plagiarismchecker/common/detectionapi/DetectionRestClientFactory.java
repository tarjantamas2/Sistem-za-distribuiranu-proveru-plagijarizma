package rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.ServiceEndpoints;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.ServiceMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.discoveryapi.DiscoveryRestClient;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.util.TempFileUtil;
import uk.co.jemos.podam.api.PodamFactory;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetectionRestClientFactory {

  private final RestTemplate restTemplate;

  private final TempFileUtil tempFileUtil;

  private final DiscoveryRestClient discoveryRestClient;

  private final PodamFactory podamFactory;

  private final Map<String, DetectionRestClient> serviceIdToDetectionRestClient = new HashMap<>();

  public DetectionRestClient getInstance(String serviceId) {
    return serviceIdToDetectionRestClient.computeIfAbsent(serviceId, serviceIdParam -> {
      ServiceMetadata serviceMetadata = discoveryRestClient.findById(serviceIdParam);
      if (serviceMetadata == null) {
        log.error("Could not construct detection service rest client for the given serviceId.");
        return null;
      }

      String tasksEndpoint = serviceMetadata.resolveEndpoint(ServiceEndpoints.TASKS);
      String taskEndpoint = serviceMetadata.resolveEndpoint(ServiceEndpoints.TASK);
      String algorithmsEndpoint = serviceMetadata.resolveEndpoint(ServiceEndpoints.ALGORITHMS);
      return getInstance(tasksEndpoint, taskEndpoint, algorithmsEndpoint);
    });
  }

  public DetectionRestClient getInstance(String tasksEndpoint, String taskEndpoint, String algorithmsEndpoint) {
    return new DetectionRestClient(restTemplate, tempFileUtil, tasksEndpoint, taskEndpoint, algorithmsEndpoint);
  }
}
