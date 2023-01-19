package com.br.producer.services;

import com.br.producer.config.rabbitMQ.RabbitMqConfig;
import com.br.producer.config.restTemplate.RestTemplateConfig;
import com.br.producer.convert.ProductConvert;
import com.br.producer.creator.ProductCreator;
import com.br.producer.dto.ProductDTO;
import com.br.producer.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProducerServicesTest {
    final String BASE_URL = "http://localhost:8081/products";
    @Mock
    RabbitMqService rabbitMqService;
    @InjectMocks
    ProducerServices services;

    @Mock
    RestTemplateConfig restConfig;

    @Mock
    RestTemplate restTemplate;

    @Test
    void shouldReturnProductDtoWhenInsertInDb() throws JsonProcessingException {
        ProductDTO product = ProductCreator.createFakeRequest();

        Mockito.doNothing().when(rabbitMqService).sendMessage(RabbitMqConfig.EXCHANGE_NAME,"ProductQueue",product,"CREATE");

        var response = services.save(ProductConvert.dtoToEntity(product));

        assertNotNull(response);
        assertEquals(product.getName(),response.getName());

//        Assertions.assertNotNull(repResponse);
//        Assertions.assertEquals(repResponse.getName(), product.getName());
//        Assertions.assertEquals(repResponse.getId(), product.getId());
//        Assertions.assertEquals(repResponse.getBarCode(), product.getBarCode());
//        Assertions.assertEquals(repResponse.getCode(), product.getCode());
//        Assertions.assertEquals(repResponse.getCategory(), product.getCategory());
//        Assertions.assertEquals(repResponse.getMaterial(), product.getMaterial());
//        Assertions.assertEquals(repResponse.getColor(), product.getColor());
//        Assertions.assertEquals(repResponse.getFabDate(), product.getFabDate());
//        Assertions.assertEquals(repResponse.getExpDate(), product.getExpDate());
//        Assertions.assertEquals(repResponse.getStockAmount(), product.getStockAmount());
//        Assertions.assertEquals(repResponse.getPrice(), product.getPrice());
//        Assertions.assertEquals(repResponse.getSeries(), product.getSeries());

    }

    @Test
    void shouldReturnProductDtoWhenFindById(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic "+ this.services.basicAuth("user:admin"));
        var request = new HttpEntity<String>(httpHeaders);

        var product = ProductCreator.createFakeRequest().withId(1L);
        Mockito.when(restConfig.url()).thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.getForObject(restConfig.url().concat("/{id}"),ProductDTO.class, 1L)).thenReturn(product);

        ProductDTO productDTO = this.services.findById(1);
        System.out.println(product);
        assertNotNull(productDTO);
        assertEquals(product.getName(),productDTO.getName());
    }

    @Test
    void shouldDeleteProductInDb() throws ParseException, JsonProcessingException {

        ProductDTO product = ProductCreator.createFakeRequest().withId(1L);
        Mockito.when(restConfig.url()).thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.getForObject(restConfig.url().concat("/{id}"),ProductDTO.class, 1L)).thenReturn(product);
//        System.out.println(reProduct);
//        System.out.println(product);
//        assertNotNull(reProduct);

        Mockito.doNothing().when(rabbitMqService).sendMessage(RabbitMqConfig.EXCHANGE_NAME,"ProductQueue",product,"DELETE");

        services.delete(1L);

        var productCheck = services.findById(1);

        assertNotNull(productCheck);
    }

    @Test
    void shouldReturnProductDtoListWhenFindAll(){
        Product product = ProductConvert.dtoToEntity(ProductCreator.createFakeRequest());
        List<Product> list = List.of(product);
        ResponseEntity<List<Product>> reList = ResponseEntity.of(Optional.of(list));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic "+ this.services.basicAuth("user:admin"));
        var request = new HttpEntity<String>(httpHeaders);
        Mockito.when(restConfig.url()).thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.exchange(restConfig.url(),HttpMethod.GET,request,new ParameterizedTypeReference<List<Product>>() {}))
                .thenReturn(reList);



        var repResponse = services.getAll();

        assertEquals(product.getName(),repResponse.get(0).getName());
        assertEquals(product.getColor(),repResponse.get(0).getColor());
        assertEquals(product.getCategory(),repResponse.get(0).getCategory());

//        Assertions.assertNotNull(repResponse);
//        Assertions.assertEquals(repResponse.size(),1);
//        Assertions.assertNotNull(repResponse);
//        Assertions.assertEquals(repResponse.get(0).getName(), product.getName());
//        Assertions.assertEquals(repResponse.get(0).getId(), product.getId());
//        Assertions.assertEquals(repResponse.get(0).getBarCode(), product.getBarCode());
//        Assertions.assertEquals(repResponse.get(0).getCode(), product.getCode());
//        Assertions.assertEquals(repResponse.get(0).getCategory(), product.getCategory());
//        Assertions.assertEquals(repResponse.get(0).getMaterial(), product.getMaterial());
//        Assertions.assertEquals(repResponse.get(0).getColor(), product.getColor());
//        Assertions.assertEquals(repResponse.get(0).getFabDate(), product.getFabDate());
//        Assertions.assertEquals(repResponse.get(0).getExpDate(), product.getExpDate());
//        Assertions.assertEquals(repResponse.get(0).getStockAmount(), product.getStockAmount());
//        Assertions.assertEquals(repResponse.get(0).getPrice(), product.getPrice());
//        Assertions.assertEquals(repResponse.get(0).getSeries(), product.getSeries());

    }

    @Test
    void shouldReturnProductDtoWhenUpdatingProduct() throws ParseException, JsonProcessingException {
        ProductDTO productToBeUpdated = ProductCreator.createFakeRequest().withId(1l);
        System.out.println(productToBeUpdated);
        Product finalProduct = ProductConvert.dtoToEntity(ProductCreator.createFakeRequest());
        System.out.println(finalProduct);
        Mockito.when(restConfig.url()).thenReturn("http://localhost:8080/products");
        Mockito.when(restTemplate.getForObject(restConfig.url().concat("/{id}"),ProductDTO.class, 1L)).thenReturn(productToBeUpdated);
        Mockito.doNothing().when(rabbitMqService).sendMessage(RabbitMqConfig.EXCHANGE_NAME,"ProductQueue",ProductConvert.entityToDTO(finalProduct),"UPDATE");
        ProductDTO editedProduct = services.update(1L,finalProduct);
        System.out.println(editedProduct);
        assertNotNull(editedProduct);

    }
}
