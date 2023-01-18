package com.br.producer.resources;

import com.br.producer.dto.ProductDTO;
import com.br.producer.entity.Product;
import com.br.producer.services.ProducerServices;
import com.br.producer.services.RabbitMqService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProducerResources {
    @Autowired
    ProducerServices services;

    @Autowired
    RabbitMqService rabbitMqService;


    @PostMapping
    public ResponseEntity save(@RequestBody Product product) {
        Product p = services.save(product);

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

        ProductDTO p = services.update(id, product);
        System.out.println(product);
        System.out.println(p);
        return p != null ?
                ResponseEntity.ok(p):
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable long id) {

        services.delete(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/import")
    public ResponseEntity importCSV(@RequestParam("file") MultipartFile file){
        var productsList = services.insertByCSV(file);

        return ResponseEntity.ok(productsList);
    }

    @PutMapping("/change-stock/{id}")
    public ResponseEntity changeStock(@PathVariable long id, @RequestBody Integer stock){
        services.changeStock(id,stock);

        return ResponseEntity.ok().build();
    }
}
