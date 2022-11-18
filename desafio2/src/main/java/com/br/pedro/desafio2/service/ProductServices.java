package com.br.pedro.desafio2.service;

import com.br.pedro.desafio2.dto.ProductDTO;
import com.br.pedro.desafio2.entity.Product;
import com.br.pedro.desafio2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServices {
    @Autowired
    ProductRepository rep;


    public void addToDb(Product product){

        rep.save(product);
    }

    public List<ProductDTO> getAll(){
        List<ProductDTO> list = rep.findAll().stream().map(ProductDTO::create).collect(Collectors.toList());
        return list;
    }

    public Optional<ProductDTO> findById(long id){
        Optional<Product> product = rep.findById(id);
        return product.map(ProductDTO::create);
    }

    public ProductDTO update(long id, Product product){
        Optional<Product> optProductToEdit = rep.findById(id);

        Product productToEdit = optProductToEdit.get();

        productToEdit.setName(product.getName());
        productToEdit.setPrice(product.getPrice());
        productToEdit.setCategory(product.getCategory());
        productToEdit.setStockAmount(product.getStockAmount());
        addToDb(productToEdit);

        return ProductDTO.create(productToEdit);
    }

    public void delete(long id){
        Optional<Product> optProductToDelete = rep.findById(id);
        Product productToDelete = optProductToDelete.get();
        rep.delete(productToDelete);
    }
}
