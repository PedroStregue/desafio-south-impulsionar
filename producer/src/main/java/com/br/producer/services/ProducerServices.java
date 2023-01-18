package com.br.producer.services;

import com.br.producer.config.rabbitMQ.RabbitMqConfig;
import com.br.producer.config.restTemplate.RestTemplateConfig;
import com.br.producer.convert.ProductConvert;
import com.br.producer.dto.ProductDTO;
import com.br.producer.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProducerServices {
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RestTemplateConfig restTemplateConfig;
    @Autowired
    RabbitMqService rabbitMqService;

    @Autowired
    ObjectMapper objectMapper;

    public String basicAuth(String auth){
        var authBytes = auth.getBytes();
        var base64AuthBytes = Base64.encodeBase64(authBytes);
        return new String(base64AuthBytes);
    }

    public Product save(Product product) {

        try {
            rabbitMqService.sendMessage(RabbitMqConfig.EXCHANGE_NAME,"ProductQueue",ProductConvert.entityToDTO(product),"CREATE");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return product;
    }

    public ProductDTO update(long id, Product product){
        ProductDTO productToEdit = this.findById(id);

        Product editedProduct = updateProduct(product,ProductConvert.dtoToEntity(productToEdit));

        try {
            rabbitMqService.sendMessage(RabbitMqConfig.EXCHANGE_NAME,"ProductQueue",ProductConvert.entityToDTO(editedProduct),"UPDATE");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return productToEdit;
    }

    public Product updateProduct(Product productToEdit, Product actualProduct){
        productToEdit.setName(actualProduct.getName());
        productToEdit.setPrice(actualProduct.getPrice());
        productToEdit.setCategory(actualProduct.getCategory());
        productToEdit.setStockAmount(actualProduct.getStockAmount());
        productToEdit.setDescription(actualProduct.getDescription());
        productToEdit.setColor(actualProduct.getColor());
        productToEdit.setSeries(actualProduct.getSeries());
        productToEdit.setMaterial(actualProduct.getMaterial());
        productToEdit.setExpDate(actualProduct.getExpDate());
        productToEdit.setFabDate(actualProduct.getFabDate());
        return productToEdit;
    }

    public ProductDTO findById(long id){
       HttpHeaders httpHeader = new HttpHeaders();
       httpHeader.add("Authorization","Basic " + this.basicAuth("user:admin"));
       var request = new HttpEntity<String>(httpHeader);

       ResponseEntity<Product> product = restTemplate.exchange(restTemplateConfig.url().concat("/").concat(String.valueOf(id)), HttpMethod.GET, request, new ParameterizedTypeReference<Product>() {});

       return ProductConvert.entityToDTO(product.getBody());
    }

    public List<ProductDTO> getAll(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic "+ this.basicAuth("user:admin"));
        var request = new HttpEntity<String>(httpHeaders);
        ResponseEntity<List<Product>> products = restTemplate.exchange(restTemplateConfig.url(), HttpMethod.GET, request, new ParameterizedTypeReference<List<Product>>() {});

        return products.getBody().stream().map(ProductConvert::entityToDTO).collect(Collectors.toList());
    }

    public void delete(long id) {
        ProductDTO product = this.findById(id);

        try {
            rabbitMqService.sendMessage(RabbitMqConfig.EXCHANGE_NAME,"ProductQueue",product,"DELETE");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductDTO> insertByCSV(MultipartFile file) {
        List<ProductDTO> list = new ArrayList<>();
        try{
            CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8")));
            String[] headers = csvReader.readNext();
            for(String header:headers){
                System.out.println(header);
            }

            List<List<String>> lines = new ArrayList<List<String>>();
            String [] columns;

            while((columns = csvReader.readNext()) != null){
                lines.add(Arrays.asList(columns));
            }
            lines.forEach(line -> {
                System.out.println(line + "-");
                String code = line.get(0);
                String barCode = line.get(1);
                String series = line.get(2);
                String name = line.get(3);
                String description = line.get(4);
                String category = line.get(5);
                Double value = Double.valueOf(line.get(6).replace(',','.'));
                System.out.println(value);
                Double tax = Double.valueOf(line.get(7).replace(',','.'))/100 + 1;
                System.out.println(tax);
                Double finalValue = value*tax;
                System.out.println(finalValue);
                String fabDate = line.get(8);
                String expDate = line.get(9);
                String color = line.get(10);
                String material = line.get(11);
                Product product = Product.builder().code(code).barCode(barCode).series(series).name(name)
                        .description(description).category(category).price(finalValue).fabDate(fabDate)
                        .expDate(expDate).color(color).material(material).stockAmount(0).build();
                list.add(ProductConvert.entityToDTO(product));
                try {
                    rabbitMqService.sendMessage(RabbitMqConfig.EXCHANGE_NAME,"ProductQueue",ProductConvert.entityToDTO(product),"CREATE");
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public void changeStock(long id, Integer stock) {
        ProductDTO productDTO = this.findById(id);
        productDTO.setStockAmount(stock);
        try {
            rabbitMqService.sendMessage(RabbitMqConfig.EXCHANGE_NAME, "ProductQueue",productDTO,"PRODUCT_CHANGED");
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
