package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.Algorithm;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.TaskResult;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.detectionapi.FindAllAlgorithmsResponse;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.DocumentMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository.DocumentRepository;

@Service
@RequiredArgsConstructor
public class AlgorithmsService {

  private final ModelMapper modelMapper;

  private final List<Algorithm> algorithms = Collections.singletonList(new Algorithm("1", "Text plagiarism detection",
      "Use this algorithm for documents that have a text/plain representation. We attempt to automatically extract text/plain representation from PDF documents.",
      Collections.singletonList("text/plain")));

  private final DocumentRepository documentRepository;

  public FindAllAlgorithmsResponse findAll() {
    return modelMapper.map(algorithms, FindAllAlgorithmsResponse.class);
  }

  public Optional<Algorithm> findById(String id) {
    return algorithms.stream().filter(a -> StringUtils.equals(id, a.getId())).findFirst();
  }

  public List<TaskResult> run(String id, String fileName, List<String> referenceFileNames,
      Map<String, DocumentMetadata> representationFileNameToMetadata) throws Exception {
    if (id.equals("1")) {
      return runAlg1(fileName, referenceFileNames, representationFileNameToMetadata);
    }

    return Collections.emptyList();
  }

  private List<TaskResult> runAlg1(String fileName, List<String> referenceFileNames,
      Map<String, DocumentMetadata> fileNameToMetadata) throws Exception {
    fileName = documentRepository.resolvePath(fileName);
    referenceFileNames = referenceFileNames.stream().map(documentRepository::resolvePath).collect(Collectors.toList());

    List<String> command = new ArrayList<>();
    command.add("python3");
    command.add("D:\\Dev\\plag-node\\protocol\\externals\\cosinechecker\\compare.py");
    command.add(fileName);
    command.addAll(referenceFileNames);

    Process process = new ProcessBuilder().command(command).start();
    process.waitFor();
    List<String> lines = IOUtils.readLines(process.getInputStream(), StandardCharsets.UTF_8);

    return lines.stream().map(line -> {
      if (!line.contains(",")) {
        return null;
      }
      String[] pathAndScore = line.split(",");
      DocumentMetadata documentMetadata = fileNameToMetadata.get(Paths.get(pathAndScore[0]).getFileName().toString());
      return new TaskResult(documentMetadata.getId(), documentMetadata.getMd5(),
          Math.round(Double.parseDouble(pathAndScore[1]) * 100D) / 100D, null);
    }).filter(Objects::nonNull).collect(Collectors.toList());
  }
}
