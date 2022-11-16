package com.br.pedro.desafio2.resources;

import com.br.pedro.desafio2.entity.Product;
import com.br.pedro.desafio2.service.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductServices services;


    @PostMapping
    public void save(@RequestBody Product product){
        services.addToDb(product);
    }

    @GetMapping
    public List<Product> listAll(){
        return services.getAll();
    }
}
