package com.br.pedro.desafio2.service;

import com.br.pedro.desafio2.entity.Product;
import com.br.pedro.desafio2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
