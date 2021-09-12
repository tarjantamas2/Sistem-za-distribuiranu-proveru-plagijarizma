package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class RestException extends RuntimeException {

  private final HttpStatus httpStatus;

  public RestException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
