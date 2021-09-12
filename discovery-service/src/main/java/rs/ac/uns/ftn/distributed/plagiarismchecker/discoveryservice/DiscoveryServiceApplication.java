package rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
    basePackages = { "rs.ac.uns.ftn.distributed.plagiarismchecker.discoveryservice",
        "rs.ac.uns.ftn.springcommonrestexceptions", "rs.ac.uns.ftn.distributed.plagiarismchecker.common" })
public class DiscoveryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DiscoveryServiceApplication.class, args);
  }
}
