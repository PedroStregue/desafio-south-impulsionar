package com.br.pedro.desafio2.resources;

import com.br.pedro.desafio2.convert.ProductConvert;
import com.br.pedro.desafio2.creator.ProductCreator;
import com.br.pedro.desafio2.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String URL="/products";

    @Test
    void getAllShouldReturnProductDto() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("name1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].price").value("10.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].stockAmount").value("10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").value("categoria1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].barCode").value("111111111"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].code").value("111111111111"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].color").value("color1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].series").value("series1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].fabDate").value("11/11/1111"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].expDate").value("11/11/1111"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].material").value("material1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].description").value("description1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[7].id").doesNotExist());
    }

    @Test
    void insertShouldReturnStatusCreated() throws Exception {
        var product = ProductCreator.createFixedRequest();
        var jsonBody = objectMapper.writeValueAsString(product);
        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/products/7"));

    }

    @Test
    void findByIdShouldReturnProductDto() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{id}"), 1)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("name1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.price").value("10.0"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.stockAmount").value("10"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.category").value("categoria1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.barCode").value("111111111"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("111111111111"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.color").value("color1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.series").value("series1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.fabDate").value("11/11/1111"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.expDate").value("11/11/1111"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.material").value("material1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("description1"));
    }

    @Test
    void deleteShouldReturnStatusOk() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"),1));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        var getAllList = mockMvc.perform(MockMvcRequestBuilders.get(URL)
                .contentType(MediaType.APPLICATION_JSON));
        getAllList.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(2));
    }

    @Test
    void editShouldReturnStatusOk() throws Exception {
        var product = ProductCreator.updateRequest();
        var jsonBody = objectMapper.writeValueAsString(product);
        var result = mockMvc.perform(MockMvcRequestBuilders.put(URL.concat("/{id}"),1)
                .content(jsonBody).contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value("1"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("nome editado"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.price").value("12.0"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.stockAmount").value("15"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value("descrição editada"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.material").value("material editado"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.color").value("cor editada"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.expDate").value("22/22/2222"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.fabDate").value("22/22/2222"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.series").value("serie editada"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.code").value("111111111111"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.barCode").value("111111111"));
    }

    @Test
    void shouldReturnNotFoundWhenSearchingNonExistingId() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.get(URL.concat("/{id}"), 10)
                .contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnProductDtoWhenImportingCsvFile() throws Exception {
        var fileResource = new ClassPathResource("mostruario_fabrica.csv");
        Assertions.assertNotNull(fileResource);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",fileResource.getFilename(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                fileResource.getInputStream()
        );

        Assertions.assertNotNull(multipartFile);

        var result = mockMvc.perform(MockMvcRequestBuilders
                .multipart(URL.concat("/import"))
                        .file(multipartFile));
        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].name").value("Teclado Gamer"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].price").value("163.3365"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].category").value("ESCRITÓRIO"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].barCode").value("945923371680"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].code").value("t0n75ytr"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].color").value("colorido"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].series").value("1/2017"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].fabDate").value("23/12/2016"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].expDate").value("n/a"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].material").value("plástico"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.[0].description").value("Teclado mecânico para jogos com luzes RGB"));
    }
    @Test
    void shouldReturnNotFoundWhenTryingToDeleteInvalidId() throws Exception {
        var result = mockMvc.perform(MockMvcRequestBuilders.delete(URL.concat("/{id}"),15));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isNotFound());

    }
//    @Test
//    void shouldReturnBadRequestWhenCreatingWithId() throws Exception {
//        var product = ProductCreator.createFixedRequest().withId(8L);
//        var jsonBody = objectMapper.writeValueAsString(product);
//        var result = mockMvc.perform(MockMvcRequestBuilders.post(URL)
//                .content(jsonBody)
//                .contentType(MediaType.APPLICATION_JSON));
//
//        result.andDo(MockMvcResultHandlers.print());
//        result.andDo(MockMvcResultMatchers.status().isBadRequest())
//    }
    @Test
    void editShouldReturnNotFoundWithInvalidId() throws Exception {
        var product = ProductCreator.updateRequest();
        var jsonBody = objectMapper.writeValueAsString(product);
        var result = mockMvc.perform(MockMvcRequestBuilders.put(URL.concat("/{id}"),15)
            .content(jsonBody).contentType(MediaType.APPLICATION_JSON));

        result.andDo(MockMvcResultHandlers.print());
        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    }

