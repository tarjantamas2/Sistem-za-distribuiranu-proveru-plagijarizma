package rs.ac.uns.ftn.springcommonrestexceptions.exception;

public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }

  public static void throwIf(boolean condition, String message) {
    if (condition) {
      throw new BadRequestException(message);
    }
  }
}
