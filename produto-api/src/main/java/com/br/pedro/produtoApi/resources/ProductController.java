package com.br.pedro.produtoApi.resources;

import com.br.pedro.produtoApi.dto.ProductDTO;
import com.br.pedro.produtoApi.entity.Product;
import com.br.pedro.produtoApi.service.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    ProductServices services;


    @PostMapping
    public ResponseEntity save(@RequestBody Product product) {

        ProductDTO p = services.addToDb(product);

        URI location = getUri(p.getId());
        return p != null ?
                ResponseEntity.created(location).build() :
                ResponseEntity.badRequest().build();
    }

    private URI getUri(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable long id){
        ProductDTO product = services.findById(id);

        return ResponseEntity.ok(product);
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

        var response = services.delete(id);

        return response;
    }

    @PostMapping("/import")
    public ResponseEntity importCSV(@RequestParam("file") MultipartFile file){
        var productsList = services.insertByCSV(file);

        return ResponseEntity.ok(productsList);
    }
}
