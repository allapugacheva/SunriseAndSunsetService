package com.example.sunriseandsunsetservice.config;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for rest template.
 */
@Configuration
@NoArgsConstructor
public class RestTemplateConfig {

  @Bean
  public RestTemplate restTemplate() {

    return new RestTemplate();
  }
}
