package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@ComponentScan(
    basePackages = { "rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice",
        "rs.ac.uns.ftn.distributed.plagiarismchecker.common" })
public class PlagiarismServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(PlagiarismServiceApplication.class, args);
  }
}
