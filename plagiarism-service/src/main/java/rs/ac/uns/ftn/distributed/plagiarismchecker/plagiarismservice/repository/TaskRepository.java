package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskStatus;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.task.Task;

public interface TaskRepository extends MongoRepository<Task, String> {

  List<Task> findByStatus(TaskStatus pending);
}
