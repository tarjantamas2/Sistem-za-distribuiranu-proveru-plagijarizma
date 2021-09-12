package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.repository;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.CandidateDocument;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.DocumentMetadata;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.ReferenceDocument;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.entity.document.Representation;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest.BadRequestException;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest.InternalServerException;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.processing.FullTextExtractor;

@Slf4j
@Component
public class DocumentRepository {

  private Path documentsStoragePath;

  private final Tika tika = new Tika();

  @PostConstruct
  public void init() {
    documentsStoragePath = Paths.get("documents").toAbsolutePath().normalize();
    initializeStoragePath(documentsStoragePath);
  }

  private void initializeStoragePath(Path path) {
    // try {
    // FileUtils.deleteDirectory(path.toFile());
    // Files.createDirectories(path);
    // } catch (Exception e) {
    // log.error("Could not create documents storage directory.", e);
    // throw new InternalServerException("Could not create documents storage directory.");
    // }
  }

  public String resolvePath(String fileName) {
    return documentsStoragePath.resolve(Paths.get(fileName)).toAbsolutePath().toString();
  }

  public CandidateDocument createCandidateDocument(MultipartFile file) {
    return createDocument(file, (mimeType, md5, originalFileName, extension) -> new CandidateDocument(null, mimeType,
        md5, originalFileName, extension));
  }

  public ReferenceDocument createReferenceDocument(MultipartFile file) {
    return createDocument(file, (mimeType, md5, originalFileName, extension) -> new ReferenceDocument(null, mimeType,
        md5, originalFileName, extension));
  }

  private <T extends DocumentMetadata> T createDocument(MultipartFile file,
      MetadataConstructor<T> metadataConstructor) {
    try {
      String extension = FilenameUtils.getExtension(file.getOriginalFilename());
      String storedFileName = getStoredFileName(UUID.randomUUID().toString(), extension);
      Path targetLocation = documentsStoragePath.resolve(storedFileName);
      Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
      String mimeType = getMimeType(targetLocation);
      if (mimeType == null) {
        throw new InternalServerException("Could not determine document mime type.");
      }

      T documentMetadata =
          metadataConstructor.create(mimeType, DigestUtils.md5Hex(FileUtils.openInputStream(targetLocation.toFile())),
              file.getOriginalFilename(), extension);

      documentMetadata.addRepresentation(new Representation(mimeType, storedFileName));
      createFullTextRepresentationIfNeededAndPossible(extension, targetLocation, mimeType, documentMetadata);

      return documentMetadata;
    } catch (Exception e) {
      log.error("Processing failed.", e);
      throw new BadRequestException("Document processing failed.");
    }
  }

  private <T extends DocumentMetadata> void createFullTextRepresentationIfNeededAndPossible(String extension,
      Path targetLocation, String mimeType, T documentMetadata) throws IOException, TikaException {
    String fullText = FullTextExtractor.extract(targetLocation.toFile(), mimeType, extension);
    if (StringUtils.isNotBlank(fullText)) {
      String storedFullTextName = UUID.randomUUID().toString() + ".txt";
      Path fullTextLocation = documentsStoragePath.resolve(storedFullTextName);
      FileUtils.writeStringToFile(fullTextLocation.toFile(), fullText, Charset.defaultCharset());
      documentMetadata.addRepresentation(new Representation("text/plain", storedFullTextName));
    }
  }

  private String getStoredFileName(String uuid, String extension) {
    if (StringUtils.isBlank(extension)) {
      return uuid;
    }
    return uuid + "." + extension;
  }

  private String getMimeType(Path targetLocation) {
    try {
      return tika.detect(targetLocation);
    } catch (Exception e) {
      log.error("Could not detect mimeType using Tika.", e);
      return null;
    }
  }

  public void delete(String path) {
    if (StringUtils.isBlank(path)) {
      return;
    }
    try {
      File file = new File(path);
      if (file.exists()) {
        FileUtils.delete(file);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @FunctionalInterface
  private interface MetadataConstructor<T extends DocumentMetadata> {
    T create(String mimeType, String md5, String originalFileName, String extension);
  }
}