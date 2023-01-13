package com.br.producer.entity;

import lombok.*;


@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@With
public class Product {

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