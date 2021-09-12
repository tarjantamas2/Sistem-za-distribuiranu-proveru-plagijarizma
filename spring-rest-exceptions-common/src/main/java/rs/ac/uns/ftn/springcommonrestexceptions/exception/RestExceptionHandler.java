package rs.ac.uns.ftn.springcommonrestexceptions.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.springcommonrestexceptions.dto.ApiError;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
    log.error("MethodArgumentNotValidException: ", exception);
    StringBuilder message = new StringBuilder();
    List<ObjectError> objectErrors = exception.getBindingResult().getAllErrors();

    for (ObjectError error : objectErrors) {
      message.append("[");
      if (error instanceof FieldError) {
        message.append(((FieldError) error).getField());
        message.append(" ");
      }
      message.append(error.getDefaultMessage());
      message.append("]");
    }

    return buildErrorResponse(HttpStatus.BAD_REQUEST, message.toString());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ApiError> handleNotFoundException(NotFoundException exception) {
    log.error("NotFoundException: ", exception);
    return buildErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ApiError> handleBadRequestException(BadRequestException exception) {
    log.error("BadRequestException: ", exception);
    return buildErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ApiError> handleForbiddenException(ForbiddenException exception) {
    log.error("ForbiddenException: ", exception);
    return buildErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
  }

  @ExceptionHandler(InternalServerException.class)
  public ResponseEntity<ApiError> handleInternalServerException(InternalServerException exception) {
    log.error("InternalServerException: ", exception);
    return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
        "Oops! Something unexpectedly went wrong. Please contact the support to fix this issue.");
  }

  private ResponseEntity<ApiError> buildErrorResponse(HttpStatus httpStatus, String explanation) {
    return new ResponseEntity<>(new ApiError(explanation), httpStatus);
  }
}
