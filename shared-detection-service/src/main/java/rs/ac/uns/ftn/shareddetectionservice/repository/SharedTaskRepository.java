package rs.ac.uns.ftn.shareddetectionservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import rs.ac.uns.ftn.shareddetectionservice.entity.SharedTask;

public interface SharedTaskRepository extends MongoRepository<SharedTask, String> {

  List<SharedTask> findByOwnerId(String ownerId);
}
