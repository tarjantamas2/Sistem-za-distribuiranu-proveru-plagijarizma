package rs.ac.uns.ftn.frontend.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class TempFileConfig {

  private Path tempFiles;

  @PostConstruct
  public void postConstruct() {
    tempFiles = Paths.get("tempfiles");
    try {
      Files.deleteIfExists(tempFiles);
      Files.createDirectories(tempFiles);
    } catch (Exception e) {
      log.error("Could not create temp directory: ", e);
    }
  }

  public Path resolve(String name) {
    return tempFiles.resolve(name);
  }
}
