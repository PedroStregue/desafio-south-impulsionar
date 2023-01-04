package com.br.pedro.produtoApi.service;

import com.br.pedro.produtoApi.convert.ProductConvert;
import com.br.pedro.produtoApi.dto.ProductDTO;
import com.br.pedro.produtoApi.entity.Product;
import com.br.pedro.produtoApi.exception.ObjectNotFoundException;
import com.br.pedro.produtoApi.repository.ProductRepository;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServices {
    @Autowired
    ProductRepository rep;

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public ProductDTO addToDb(Product product){
        Assert.isNull(product.getId(),"Não foi possível inserir o registro");

        product.setBarCode(generateBarCode(12));
        product.setCode(generateCode(9));

        return ProductConvert.entityToDTO(rep.save(product));
    }

    public List<ProductDTO> getAll(){
        List<ProductDTO> list = rep.findAll().stream().map(ProductConvert::entityToDTO).collect(Collectors.toList());
        return list;
    }

    public ProductDTO findById(long id){
        Optional<Product> product = rep.findById(id);

        return product.map(ProductConvert::entityToDTO).orElseThrow(() -> new ObjectNotFoundException("Produto não encontrado"));
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
            return ProductConvert.entityToDTO(productToEdit);
        }else{
            return null;
        }
    }

    public ResponseEntity delete(long id){
        Optional<Product> optProductToDelete = rep.findById(id);
        if(optProductToDelete.isPresent()){
            rep.deleteById(id);
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.notFound().build();
        }

    }

    public List<ProductDTO> insertByCSV(MultipartFile file){
        List<ProductDTO> list = new ArrayList<>();
        try{
            CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(file.getInputStream(),"UTF-8")));
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
                String fabDate = line.get(8);
                String expDate = line.get(9);
                String color = line.get(10);
                String material = line.get(11);
                Product product = Product.builder().code(code).barCode(barCode).series(series).name(name)
                        .description(description).category(category).price(finalValue).fabDate(fabDate)
                        .expDate(expDate).color(color).material(material).build();
                list.add(ProductConvert.entityToDTO(rep.save(product)));
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
