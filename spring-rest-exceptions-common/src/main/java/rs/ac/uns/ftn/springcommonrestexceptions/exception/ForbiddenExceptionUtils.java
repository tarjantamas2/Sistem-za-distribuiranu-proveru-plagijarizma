package rs.ac.uns.ftn.springcommonrestexceptions.exception;

public class ForbiddenExceptionUtils {

  public static void throwIf(boolean condition, String message) {
    if (condition) {
      throw new ForbiddenException(message);
    }
  }
}
