package rs.ac.uns.ftn.distributed.plagiarismchecker.plagiarismservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@Configuration
public class BeanConfig {

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public PodamFactory podamFactory() {
    return new PodamFactoryImpl();
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
}
