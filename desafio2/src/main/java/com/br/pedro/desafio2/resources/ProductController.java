package com.br.pedro.desafio2.resources;

import com.br.pedro.desafio2.entity.Product;
import com.br.pedro.desafio2.service.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PutMapping("/{id}")
    public void edit(@PathVariable long id,@RequestBody Product product){
        services.update(id,product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        services.delete(id);
    }
}
