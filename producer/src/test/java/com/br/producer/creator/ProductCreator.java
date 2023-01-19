package com.br.producer.creator;


import com.br.producer.dto.ProductDTO;
import com.br.producer.entity.Product;
import com.github.javafaker.Faker;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ProductCreator {

    public static ProductDTO createFixedRequest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ProductDTO product = ProductDTO.builder()
                .name("nome1").code("11111111")
                .barCode("111111111111").color("azul")
                .fabDate("11/11/1111").expDate("11/11/1111")
                .series("series1").category("categoria1")
                .description("descrição1").material("Plástico")
                .stockAmount(10).price(10.00).build();
        return product;
    }

    public static ProductDTO createFakeRequest(){
        var faker = new Faker();
        ProductDTO product = ProductDTO.builder()
                .name(faker.commerce().productName())
                .category(faker.commerce().department())
                .barCode(faker.random().toString())
                .code(faker.random().toString()).color(faker.commerce().color())
                .build()
                ;

        return product;
    }

    public static Product updateRequest() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Product product = Product.builder().name("nome editado")
                .price(12.0).stockAmount(15).description("descrição editada")
                .category("categoria editada").material("material editado")
                .expDate("22/22/2222").fabDate("22/22/2222").color("cor editada")
                .series("serie editada").build();
        return product;
    }
}
