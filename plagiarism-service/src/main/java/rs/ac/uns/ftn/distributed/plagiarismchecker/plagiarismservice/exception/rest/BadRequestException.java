package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest;

import org.springframework.http.HttpStatus;

public class BadRequestException extends RestException {

  public BadRequestException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
