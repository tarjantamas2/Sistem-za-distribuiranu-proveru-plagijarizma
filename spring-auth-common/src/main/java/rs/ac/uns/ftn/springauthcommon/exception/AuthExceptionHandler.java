package rs.ac.uns.ftn.springauthcommon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import rs.ac.uns.ftn.springcommonrestexceptions.dto.ApiError;

@ControllerAdvice
public class AuthExceptionHandler {

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiError> handleBadCredentialsException() {
    return buildErrorResponse(HttpStatus.BAD_REQUEST, "Wrong email and password combination.");
  }

  private ResponseEntity<ApiError> buildErrorResponse(HttpStatus httpStatus, String explanation) {
    return new ResponseEntity<>(new ApiError(explanation), httpStatus);
  }
}
