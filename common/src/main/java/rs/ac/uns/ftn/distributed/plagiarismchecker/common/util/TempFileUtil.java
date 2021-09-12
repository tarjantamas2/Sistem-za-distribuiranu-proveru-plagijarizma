package rs.ac.uns.ftn.distributed.plagiarismchecker.common.util;

import static java.util.UUID.randomUUID;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TempFileUtil {

  private final Map<Object, List<String>> callerToTempName = new ConcurrentHashMap<>();

  public <T> T withTempFile(Function<String, T> consumer) {
    String tempFileName = newTempFileName(consumer);
    T t = consumer.apply(tempFileName);
    cleanUp(consumer);
    return t;
  }

  private String newTempFileName(Object caller) {
    String name = getRandomUnusedUUID();
    callerToTempName.computeIfAbsent(caller, key -> new ArrayList<>()).add(name);
    return name;
  }

  private void cleanUp(Object caller) {
    if (!callerToTempName.containsKey(caller)) {
      return;
    }
    for (String fileName : callerToTempName.get(caller)) {
      try {
        FileUtils.forceDelete(new File(fileName));
      } catch (Exception e) {
        log.error("Could not delete temp file [{}]: ", fileName, e);
      }
    }
  }

  private String getRandomUnusedUUID() {
    String uuid = randomUUID().toString();
    int tries = 0;

    while (true) {
      if (tries > 5) {
        throw new RuntimeException("Unable to generate unique uuid.");
      }
      boolean unique = true;
      for (List<String> value : callerToTempName.values()) {
        for (String usedUUID : value) {
          if (usedUUID.equals(uuid)) {
            tries++;
            unique = false;
          }
        }
      }
      if (unique) {
        return uuid;
      }
    }
  }
}
