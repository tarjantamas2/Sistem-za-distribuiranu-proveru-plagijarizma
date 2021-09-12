package rs.ac.uns.ftn.distributed.plagiarismchecker.common.util;

import java.util.Collections;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestClient {

  private final RestTemplate restTemplate;

  public <T> T get(String uri, Class<T> responseType) {
    try {
      ResponseEntity<T> response =
          restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, responseType, Collections.emptyMap());
      if (response.getStatusCode() != HttpStatus.OK) {
        return null;
      }

      return response.getBody();
    } catch (Exception e) {
      log.error("", e);
      return null;
    }
  }

  public <T> T get(String uri, ParameterizedTypeReference<T> responseType) {
    try {
      ResponseEntity<T> response =
          restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, responseType, Collections.emptyMap());
      if (response.getStatusCode() != HttpStatus.OK) {
        return null;
      }

      return response.getBody();
    } catch (Exception e) {
      log.error("", e);
      return null;
    }
  }
}
