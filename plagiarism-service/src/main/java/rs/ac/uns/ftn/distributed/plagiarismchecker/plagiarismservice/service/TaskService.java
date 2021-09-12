package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.Algorithm;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskStatus;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.CreateTaskRelayedRequest;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.CreateTaskResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.FindAllTasksResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.FindTaskByIdResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.CandidateDocument;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.Representation;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.task.Task;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest.BadRequestException;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest.NotFoundException;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.CandidateDocumentRepository;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.DocumentRepository;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.TaskRepository;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;

  private final DocumentRepository documentRepository;

  private final CandidateDocumentRepository candidateDocumentRepository;

  private final AlgorithmsService algorithmsService;

  private final ModelMapper modelMapper;

  @PostConstruct
  public void postConstruct() {
    // taskRepository.deleteAll();
    // candidateDocumentRepository.deleteAll();
  }

  public CreateTaskResponse create(CreateTaskRelayedRequest taskDto) {
    CandidateDocument candidateDocument =
        candidateDocumentRepository.save(documentRepository.createCandidateDocument(taskDto.getFile()));

    Algorithm algorithm = algorithmsService.findById(taskDto.getAlgorithmId())
      .orElseThrow(() -> new NotFoundException("Algorithm with the given id not found."));
    verifyMimeTypeIsSupported(algorithm, candidateDocument);

    Task task = new Task(null, candidateDocument.getId(), candidateDocument.getMd5(), TaskStatus.PENDING, null,
        taskDto.getAlgorithmId(), null);
    task.setCandidateDocumentMd5(candidateDocument.getMd5());

    return modelMapper.map(taskRepository.save(task), CreateTaskResponse.class);
  }

  private void verifyMimeTypeIsSupported(Algorithm algorithm, CandidateDocument candidateDocument) {
    for (Representation representation : candidateDocument.getRepresentations()) {
      if (algorithm.getSupportedMimeTypes().contains(representation.getMimeType())) {
        return;
      }
    }
    throw new BadRequestException(
        "The given document has no representations that are compatible with the given algorithm.");
  }

  public FindTaskByIdResponse findById(String id) {
    Task task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task with given id not found."));
    return modelMapper.map(task, FindTaskByIdResponse.class);
  }

  public FindAllTasksResponse findAll() {
    List<Task> tasks = taskRepository.findAll();
    FindAllTasksResponse response = modelMapper.map(tasks, FindAllTasksResponse.class);
    return response;
  }
}
