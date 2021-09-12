package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest;

import org.springframework.http.HttpStatus;

public class NotFoundException extends RestException {

  public NotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
