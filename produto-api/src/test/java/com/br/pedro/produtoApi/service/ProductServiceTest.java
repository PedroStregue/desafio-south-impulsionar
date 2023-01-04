package com.br.pedro.produtoApi.service;

import com.br.pedro.produtoApi.creator.ProductCreator;
import com.br.pedro.produtoApi.entity.Product;
import com.br.pedro.produtoApi.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@ExtendWith({SpringExtension.class})
public class ProductServiceTest {
    @Mock
    ProductRepository repository;
    @InjectMocks
    ProductServices services;

    @Test
    void shouldReturnProductDtoWhenInsertInDb(){
        Product product = ProductCreator.createFakeRequest();

        Mockito.when(repository.save(product)).thenReturn(product);

        var repResponse = services.addToDb(product);

        Assertions.assertNotNull(repResponse);
        Assertions.assertEquals(repResponse.getName(), product.getName());
        Assertions.assertEquals(repResponse.getId(), product.getId());
        Assertions.assertEquals(repResponse.getBarCode(), product.getBarCode());
        Assertions.assertEquals(repResponse.getCode(), product.getCode());
        Assertions.assertEquals(repResponse.getCategory(), product.getCategory());
        Assertions.assertEquals(repResponse.getMaterial(), product.getMaterial());
        Assertions.assertEquals(repResponse.getColor(), product.getColor());
        Assertions.assertEquals(repResponse.getFabDate(), product.getFabDate());
        Assertions.assertEquals(repResponse.getExpDate(), product.getExpDate());
        Assertions.assertEquals(repResponse.getStockAmount(), product.getStockAmount());
        Assertions.assertEquals(repResponse.getPrice(), product.getPrice());
        Assertions.assertEquals(repResponse.getSeries(), product.getSeries());

    }

    @Test
    void shouldDeleteProductInDb() throws ParseException {
        Product product = ProductCreator.createFixedRequest().withId(1L);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(product));
        Mockito.doNothing().when(repository).deleteById(1L);

        var response = services.delete(product.getId());
        var productsList = services.getAll();

        Assertions.assertEquals(ResponseEntity.ok().build(), response);
        Assertions.assertEquals(0, productsList.size());
    }

    @Test
    void shouldReturnProductDtoListWhenFindAll(){
        Product product = ProductCreator.createFakeRequest();

        Mockito.when(repository.findAll()).thenReturn(List.of(product));

        var repResponse = services.getAll();

        Assertions.assertNotNull(repResponse);
        Assertions.assertEquals(repResponse.size(),1);
        Assertions.assertNotNull(repResponse);
        Assertions.assertEquals(repResponse.get(0).getName(), product.getName());
        Assertions.assertEquals(repResponse.get(0).getId(), product.getId());
        Assertions.assertEquals(repResponse.get(0).getBarCode(), product.getBarCode());
        Assertions.assertEquals(repResponse.get(0).getCode(), product.getCode());
        Assertions.assertEquals(repResponse.get(0).getCategory(), product.getCategory());
        Assertions.assertEquals(repResponse.get(0).getMaterial(), product.getMaterial());
        Assertions.assertEquals(repResponse.get(0).getColor(), product.getColor());
        Assertions.assertEquals(repResponse.get(0).getFabDate(), product.getFabDate());
        Assertions.assertEquals(repResponse.get(0).getExpDate(), product.getExpDate());
        Assertions.assertEquals(repResponse.get(0).getStockAmount(), product.getStockAmount());
        Assertions.assertEquals(repResponse.get(0).getPrice(), product.getPrice());
        Assertions.assertEquals(repResponse.get(0).getSeries(), product.getSeries());

    }

    @Test
    void shouldReturnProductDtoWhenUpdatingProduct() throws ParseException {
        Product product = ProductCreator.createFixedRequest().withId(1L);
        Product productToEdit = ProductCreator.updateRequest().withId(1L);

        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(product));
        var editedProduct = services.update(product.getId(), productToEdit);

        Assertions.assertEquals(product.getId(),editedProduct.getId());
        Assertions.assertEquals(product.getBarCode(), editedProduct.getBarCode());
        Assertions.assertEquals(product.getCode(), editedProduct.getCode());
        Assertions.assertEquals("nome editado", editedProduct.getName());
        Assertions.assertEquals(12.0, editedProduct.getPrice());
        Assertions.assertEquals(15, editedProduct.getStockAmount());
        Assertions.assertEquals("material editado", editedProduct.getMaterial());
        Assertions.assertEquals("categoria editada", editedProduct.getCategory());
        Assertions.assertEquals("cor editada", editedProduct.getColor());
        Assertions.assertEquals("22/22/2222", editedProduct.getExpDate());
        Assertions.assertEquals("22/22/2222", editedProduct.getFabDate());
        Assertions.assertEquals("serie editada", editedProduct.getSeries());
        Assertions.assertEquals("descrição editada", editedProduct.getDescription());
    }

//    @Test
//    void shouldReturnProductDTOListWhenImportingCsv() throws IOException, ParseException {
//        Product product = ProductCreator.createFixedRequest();
//        var fileResource = new ClassPathResource("mostruario_fabrica.csv");
//        Assertions.assertNotNull(fileResource);
//
//        MockMultipartFile multipartFile = new MockMultipartFile(
//                "file",fileResource.getFilename(),
//                MediaType.MULTIPART_FORM_DATA_VALUE,
//                fileResource.getInputStream()
//        );
//
//        Mockito.when(repository.save(product)).thenReturn(product);
//        var productList = services.insertByCSV(multipartFile);
//
//        Assertions.assertNotNull(productList.get(0).getId());
//        Assertions.assertEquals(product.getCode(),productList.get(0).getCode());
//    }
}
