package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.exception.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.dto.ApiError;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(RestException.class)
  public ResponseEntity<ApiError> handleRestException(RestException exception) {
    log.error("RestException occurred:", exception);
    return new ResponseEntity<>(new ApiError(exception.getMessage()), exception.getHttpStatus());
  }
}
