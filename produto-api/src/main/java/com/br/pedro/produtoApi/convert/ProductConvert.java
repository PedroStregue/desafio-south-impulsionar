package com.br.pedro.produtoApi.convert;

import com.br.pedro.produtoApi.dto.ProductDTO;
import com.br.pedro.produtoApi.entity.Product;

public class ProductConvert {

    public static Product dtoToEntity(ProductDTO productDTO){
        return Product.builder()
                .name(productDTO.getName())
                .barCode(productDTO.getBarCode())
                .code(productDTO.getCode())
                .category(productDTO.getCategory())
                .color(productDTO.getColor())
                .description(productDTO.getDescription())
                .expDate(productDTO.getExpDate())
                .fabDate(productDTO.getFabDate())
                .id(productDTO.getId())
                .price(productDTO.getPrice())
                .material(productDTO.getMaterial())
                .series(productDTO.getSeries())
                .stockAmount(productDTO.getStockAmount())
                .build();
    }

    public static ProductDTO entityToDTO(Product product){
        return ProductDTO.builder()
                .name(product.getName())
                .barCode(product.getBarCode())
                .code(product.getCode())
                .category(product.getCategory())
                .color(product.getColor())
                .description(product.getDescription())
                .expDate(product.getExpDate())
                .fabDate(product.getFabDate())
                .id(product.getId())
                .price(product.getPrice())
                .material(product.getMaterial())
                .series(product.getSeries())
                .stockAmount(product.getStockAmount())
                .build();
    }
}
