package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rs.ac.uns.ftn.distributed.plagiarismchecker.common.discoveryapi.DiscoveryRestClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DiscoveryConfig {

  @Value("${server.port}")
  public int port;

  @Value("${spring.application.name}")
  public String appName;

  private final DiscoveryRestClient discoveryRestClient;

  @EventListener(ApplicationStartedEvent.class)
  public void postConstruct() {
    discoveryRestClient.register(port, appName);
  }
}
