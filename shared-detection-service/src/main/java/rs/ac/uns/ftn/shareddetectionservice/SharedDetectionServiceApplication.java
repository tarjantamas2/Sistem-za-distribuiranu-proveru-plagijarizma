package rs.ac.uns.ftn.shareddetectionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@ComponentScan(
    basePackages = { "rs.ac.uns.ftn.distributed.plagiarismchecker.common", "rs.ac.uns.ftn.shareddetectionservice",
        "rs.ac.uns.ftn.springauthcommon", "rs.ac.uns.ftn.springcommonrestexceptions",
        "rs.ac.uns.ftn.springcommonrestexceptions" })
@EnableMongoRepositories(basePackages = { "rs.ac.uns.ftn.springauthcommon", "rs.ac.uns.ftn.shareddetectionservice" })
public class SharedDetectionServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SharedDetectionServiceApplication.class, args);
  }
}
