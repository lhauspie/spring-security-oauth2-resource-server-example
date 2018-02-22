package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

//  @Bean
//  public RestTemplate restTemplate(@Autowired ObjectMapper objectMapper) {
//    RestTemplate restTemplate = new RestTemplate();
//    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//    converter.setObjectMapper(objectMapper);
//    restTemplate.getMessageConverters().add(0, converter);
//    return restTemplate;
//  }

}
