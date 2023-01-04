package com.br.pedro.produtoApi.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@With
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double price;
    private Integer stockAmount;
    private String category;
    private String barCode;
    private String code;
    private String color;
    private String series;
    @Column(name = "fabrication_date")
    private String fabDate;
    @Column(name = "expiration_date")
    private String expDate;
    private String material;
    private String description;


}
