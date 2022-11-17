package com.br.pedro.desafio2.service;

import com.br.pedro.desafio2.entity.Product;
import com.br.pedro.desafio2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServices {
    @Autowired
    ProductRepository rep;


    public void addToDb(Product product){

        rep.save(product);
    }

    public List<Product> getAll(){

        return rep.findAll();
    }

    public Optional<Product> findById(long id){
        return rep.findById(id);
    }

    public void update(long id, Product product){
        Optional<Product> optProductToEdit = rep.findById(id);
        Product productToEdit = optProductToEdit.get();
        productToEdit.setName(product.getName());
        productToEdit.setPrice(product.getPrice());
        productToEdit.setCategory(product.getCategory());
        productToEdit.setStockAmount(product.getStockAmount());
        addToDb(productToEdit);
    }

    public void delete(long id){
        Optional<Product> optProductToDelete = rep.findById(id);
        Product productToDelete = optProductToDelete.get();
        rep.delete(productToDelete);
    }
}
