package com.br.pedro.desafio2.service;

import com.br.pedro.desafio2.dto.ProductDTO;
import com.br.pedro.desafio2.entity.Product;
import com.br.pedro.desafio2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServices {
    @Autowired
    ProductRepository rep;


    public ProductDTO addToDb(Product product){
        Assert.isNull(product.getId(),"Não foi possível inserir o registro");


        return ProductDTO.create(rep.save(product));
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
        Assert.notNull(id,"Não foi possível atualizar o registro");

        Optional<Product> optProductToEdit = rep.findById(id);

        if(optProductToEdit.isPresent()) {

            Product productToEdit = optProductToEdit.get();

            productToEdit.setName(product.getName());
            productToEdit.setPrice(product.getPrice());
            productToEdit.setCategory(product.getCategory());
            productToEdit.setStockAmount(product.getStockAmount());
            productToEdit.setCode(product.getCode());
            productToEdit.setBarCode(product.getBarCode());
            productToEdit.setDescription(product.getDescription());
            productToEdit.setColor(product.getColor());
            productToEdit.setSeries(product.getSeries());
            productToEdit.setMaterial(product.getMaterial());
            productToEdit.setExpDate(product.getExpDate());
            productToEdit.setFabDate(product.getFabDate());
            rep.save(productToEdit);
            return ProductDTO.create(productToEdit);
        }else{
            return null;
        }
    }

    public void delete(long id){
        Optional<Product> optProductToDelete = rep.findById(id);
        Product productToDelete = optProductToDelete.get();
        rep.delete(productToDelete);
    }
}
