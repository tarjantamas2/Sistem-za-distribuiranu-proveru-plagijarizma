package rs.ac.uns.ftn.shareddetectionservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscoveryConfig {

  @Value("${ftn.discovery.services-endpoint}")
  public String servicesUri;

  @Value("${ftn.discovery.service-endpoint}")
  public String serviceUri;
}
