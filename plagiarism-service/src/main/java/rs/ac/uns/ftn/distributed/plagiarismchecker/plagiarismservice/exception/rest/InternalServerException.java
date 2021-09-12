package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest;

import org.springframework.http.HttpStatus;

public class InternalServerException extends RestException {

  public InternalServerException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }
}
