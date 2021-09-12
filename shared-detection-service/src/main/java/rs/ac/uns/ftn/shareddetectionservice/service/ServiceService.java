package rs.ac.uns.ftn.shareddetectionservice.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.ServiceMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.DetectionRestClient;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.DetectionRestClientFactory;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.FindAllAlgorithmsResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.discoveryapi.DiscoveryRestClient;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi.ServiceMetadataWithAlgorithms;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceService {

  private final DiscoveryRestClient discoveryRestClient;

  private final DetectionRestClientFactory detectionRestClientFactory;

  private final ModelMapper modelMapper;

  public List<ServiceMetadataWithAlgorithms> findAll() {
    List<ServiceMetadata> serviceMetadataList = discoveryRestClient.findAll();

    return serviceMetadataList.stream().map(this::fetchAlgorithms).collect(Collectors.toList());
  }

  private ServiceMetadataWithAlgorithms fetchAlgorithms(ServiceMetadata serviceMetadata) {
    DetectionRestClient restClient = detectionRestClientFactory.getInstance(serviceMetadata.getId());
    ServiceMetadataWithAlgorithms result = modelMapper.map(serviceMetadata, ServiceMetadataWithAlgorithms.class);

    try {
      FindAllAlgorithmsResponse algorithms = restClient.findAllAlgorithms().getBody();
      result.setAlgorithms(algorithms);
    } catch (Exception e) {
      log.error("Could not reach service: ", e);
      result.setAlgorithms(Collections.emptyList());
    }

    return result;
  }
}
