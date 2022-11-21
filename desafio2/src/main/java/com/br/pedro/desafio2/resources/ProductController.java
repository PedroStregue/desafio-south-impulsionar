package com.br.pedro.desafio2.resources;

import com.br.pedro.desafio2.dto.ProductDTO;
import com.br.pedro.desafio2.entity.Product;
import com.br.pedro.desafio2.service.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductServices services;


    @PostMapping
    public ResponseEntity save(@RequestBody Product product) {

        services.addToDb(product);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity listAll() {

        return ResponseEntity.ok(services.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable long id, @RequestBody Product product) {
        product.setId(id);

        ProductDTO p = services.update(id, product);

        return p != null ?
                ResponseEntity.ok(p):
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {

        services.delete(id);

        return ResponseEntity.ok().build();
    }
}
