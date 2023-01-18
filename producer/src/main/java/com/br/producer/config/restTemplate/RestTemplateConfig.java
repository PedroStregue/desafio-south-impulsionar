package com.br.producer.config.restTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Value("${url.produto-api}")
    private String url;
    @Value("${url.port}")
    private String port;

    public String url(){
        return "http://".concat(url).concat(":").concat(port).concat("/products");
    }

    @Bean
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        MappingJackson2HttpMessageConverter convert = new MappingJackson2HttpMessageConverter();
        convert.setObjectMapper(new ObjectMapper());
        restTemplate.getMessageConverters().add(convert);
        return restTemplate;
    }
}
