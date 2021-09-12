package rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.ServiceEndpoints;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.DetectionRestClient;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.DetectionRestClientFactory;
import rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.entity.ServiceMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.repository.ServiceRepository;
import rs.ac.uns.ftn.springcommonrestexceptions.exception.NotFoundException;

@Service
@RequiredArgsConstructor
public class ServiceService {

  private final ServiceRepository serviceRepository;

  private final DetectionRestClientFactory detectionRestClientFactory;

  public ServiceMetadata register(ServiceMetadata serviceMetadata, String remoteAddr) {
    serviceMetadata.setAddress(remoteAddr);
    DetectionRestClient restClient = detectionRestClientFactory.getInstance(
        serviceMetadata.resolveEndpoint(ServiceEndpoints.TASKS), serviceMetadata.resolveEndpoint(ServiceEndpoints.TASK),
        serviceMetadata.resolveEndpoint(ServiceEndpoints.ALGORITHMS));
    restClient.testRun();

    ServiceMetadata existing = serviceRepository.findByAddressAndPort(remoteAddr, serviceMetadata.getPort());
    if (existing != null) {
      existing.setDescription(serviceMetadata.getDescription());
      return serviceRepository.save(existing);
    }

    return serviceRepository.save(serviceMetadata);
  }

  public List<ServiceMetadata> findAll() {
    return serviceRepository.findAll();
  }

  public ServiceMetadata findById(String id) {
    return serviceRepository.findById(id)
      .orElseThrow(() -> new NotFoundException("Service with given id not found: " + id));
  }
}
