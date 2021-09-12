package rs.ac.uns.ftn.springcommonrestexceptions.exception;

public class InternalServerException extends RuntimeException {

  public InternalServerException(String message) {
    super(message);
  }

  public InternalServerException(Throwable cause) {
    super(cause);
  }
}
