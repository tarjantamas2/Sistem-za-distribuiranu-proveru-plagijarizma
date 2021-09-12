package rs.ac.uns.ftn.shareddetectionservice.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@Configuration
public class BeanConfig {

  @Bean
  public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    return restTemplate;
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  public PodamFactory podamFactory() {
    return new PodamFactoryImpl();
  }
}
