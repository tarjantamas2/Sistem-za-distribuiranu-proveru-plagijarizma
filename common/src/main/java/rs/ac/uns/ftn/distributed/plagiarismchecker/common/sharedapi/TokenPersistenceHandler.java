package rs.ac.uns.ftn.distributed.plagiarismchecker.common.sharedapi;

public interface TokenPersistenceHandler {

  void saveToken(String token);

  String getToken();
}
