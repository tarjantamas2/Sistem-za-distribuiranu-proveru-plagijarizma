package rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice.entity.ServiceMetadata;

public interface ServiceRepository extends MongoRepository<ServiceMetadata, String> {

  ServiceMetadata findByAddressAndPort(String address, String port);
}
