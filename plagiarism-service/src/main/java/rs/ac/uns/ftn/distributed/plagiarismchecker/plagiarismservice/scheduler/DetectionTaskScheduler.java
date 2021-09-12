package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.scheduler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.Algorithm;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResult;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskStatus;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.DocumentMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.Representation;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.task.Task;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.CandidateDocumentRepository;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.ReferenceDocumentRepository;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.TaskRepository;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.service.AlgorithmsService;

@Slf4j
@Component
@RequiredArgsConstructor
public class DetectionTaskScheduler {

  public static final int APPROXIMATE_THREAD_COUNT = 3;

  private final ReferenceDocumentRepository referenceDocumentRepository;

  private final CandidateDocumentRepository candidateDocumentRepository;

  private final TaskRepository taskRepository;

  private final AlgorithmsService algorithmsService;

  private boolean running = false;

  @Scheduled(cron = "0/10 * * * * *") // every 10 seconds
  public void scheduleTasks() {
    if (running) {
      return;
    }
    running = true;
    processPendingTasks();
    running = false;
  }

  private void processPendingTasks() {
    List<Task> tasks = taskRepository.findByStatus(TaskStatus.PENDING);
    if (CollectionUtils.isEmpty(tasks)) {
      return;
    }

    tasks.forEach(task -> task.setStatus(TaskStatus.RUNNING));
    taskRepository.saveAll(tasks);

    tasks.forEach(this::processTask);
  }

  private void processTask(Task task) {
    Algorithm algorithm = algorithmsService.findById(task.getAlgorithmId()).orElse(null);
    if (algorithm == null) {
      failTask(task, "Algorithm not available.");
      return;
    }

    DocumentMetadata candidateDocument =
        candidateDocumentRepository.findById(task.getCandidateDocumentId()).orElse(null);
    if (candidateDocument == null) {
      failTask(task, "Candidate document not found.");
      return;
    }

    String compatibleMimeType = getCompatibleMimeType(algorithm, candidateDocument);
    if (compatibleMimeType == null) {
      failTask(task, "Algorithm does not support any of the representations available for the candidate document.");
      return;
    }

    List<DocumentMetadata> referenceDocuments =
        referenceDocumentRepository.findByRepresentationsMimeType(compatibleMimeType);

    List<String> referenceFileNames = referenceDocuments.stream()
      .map(dm -> dm.getRepresentation(compatibleMimeType).getFileName())
      .collect(Collectors.toList());

    Map<String, DocumentMetadata> representationFileNameToMetadata = referenceDocuments.stream()
      .collect(Collectors.toMap(doc -> doc.getRepresentation(compatibleMimeType).getFileName(), doc -> doc));

    try {
      List<TaskResult> results = algorithmsService.run(algorithm.getId(), candidateDocument.getFileName(),
          referenceFileNames, representationFileNameToMetadata);
      task.setResults(results);
      task.setStatus(TaskStatus.COMPLETED);
      taskRepository.save(task);
    } catch (Exception e) {
      e.printStackTrace();
      failTask(task, "Exception during detection.");
    }
  }

  private String getCompatibleMimeType(Algorithm algorithm, DocumentMetadata documentMetadata) {
    for (String supportedMimeType : algorithm.getSupportedMimeTypes()) {
      for (Representation representation : documentMetadata.getRepresentations()) {
        if (representation.getMimeType().equals(supportedMimeType)) {
          return supportedMimeType;
        }
      }
    }
    return null;
  }

  private void failTask(Task task, String description) {
    task.setStatus(TaskStatus.FAILED);
    task.setStatusDescription(description);
    taskRepository.save(task);
  }
}