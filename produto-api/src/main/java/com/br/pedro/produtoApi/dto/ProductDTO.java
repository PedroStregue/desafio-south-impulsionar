package com.br.pedro.produtoApi.dto;

import com.br.pedro.produtoApi.entity.Product;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.modelmapper.ModelMapper;

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


    public static ProductDTO create(Product product){
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(product, ProductDTO.class);
    }
}
