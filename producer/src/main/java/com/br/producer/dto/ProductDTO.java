package com.br.producer.dto;


import lombok.Builder;
import lombok.Data;
import lombok.With;


@Data
@Builder
@With
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer stockAmount;
    private String category;
    private String barCode;
    private String code;
    private String color;
    private String series;
    private String fabDate;
    private String expDate;
    private String material;
    private String description;

}
