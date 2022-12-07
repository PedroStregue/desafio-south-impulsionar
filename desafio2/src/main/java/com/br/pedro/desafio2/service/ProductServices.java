package com.br.pedro.desafio2.service;

import com.br.pedro.desafio2.dto.ProductDTO;
import com.br.pedro.desafio2.entity.Product;
import com.br.pedro.desafio2.repository.ProductRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServices {
    @Autowired
    ProductRepository rep;


    public ProductDTO addToDb(Product product){
        Assert.isNull(product.getId(),"Não foi possível inserir o registro");

        product.setBarCode(generateBarCode(12));
        product.setCode(generateCode(9));

        return ProductDTO.create(rep.save(product));
    }

    public List<ProductDTO> getAll(){
        List<ProductDTO> list = rep.findAll().stream().map(ProductDTO::create).collect(Collectors.toList());
        return list;
    }

    public void findById(long id){
        Optional<Product> product = rep.findById(id);

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

    public List<ProductDTO> insertByCSV(InputStream is){
        List<ProductDTO> list = new ArrayList<>();
        try{
            CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(is,"UTF-8")));
            String[] headers = csvReader.readNext();
            for(String header:headers){
                System.out.println(header);
            }

            List<List<String>> lines = new ArrayList<List<String>>();
            String [] columns;

            while((columns = csvReader.readNext()) != null){
                lines.add(Arrays.asList(columns));
            }
            lines.forEach(line -> {
                System.out.println(line + "-");
                String code = line.get(0);
                String barCode = line.get(1);
                String series = line.get(2);
                String name = line.get(3);
                String description = line.get(4);
                String category = line.get(5);
                Double value = Double.valueOf(line.get(6).replace(',','.'));
                System.out.println(value);
                Double tax = Double.valueOf(line.get(7).replace(',','.'))/100 + 1;
                System.out.println(tax);
                Double finalValue = value*tax;
                System.out.println(finalValue);
                String fabricationDate = line.get(8);
                String expirationDate = line.get(9);
                String color = line.get(10);
                String material = line.get(11);
                Product product = Product.builder().code(code).barCode(barCode).series(series).name(name)
                        .description(description).category(category).price(finalValue).fabDate(fabricationDate)
                        .expDate(expirationDate).color(color).material(material).build();
                list.add(ProductDTO.create(rep.save(product)));
            });
        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    public String generateBarCode(int size){
        String numericStr = "0123456789";
        StringBuilder s = new StringBuilder(size);

        int i;

        for(i=0; i<size; i++){
            int n = (int)(numericStr.length() * Math.random());
            s.append(numericStr.charAt(n));
        }
        return s.toString();

    }

    public String generateCode(int size){
        String alphaNumericStr = "0123456789abcdefghijklmnopqrstuvxwyz";
        StringBuilder s = new StringBuilder(size);

        int i;

        for(i=0; i<size; i++){
            int n = (int)(alphaNumericStr.length() * Math.random());
            s.append(alphaNumericStr.charAt(n));
        }
        return s.toString();

    }
}
