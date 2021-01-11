package com.rufino.server;

import com.rufino.server.model.Product;
import com.rufino.server.service.ProductService;

import org.hamcrest.core.Is;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ServerUpdateTests {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductService productService;

    @BeforeEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM PRODUCTS");
    }

    @Test
    void itShouldUpdateProduct() throws Exception {
        JSONObject my_obj = new JSONObject();

        Product product1 = new Product();
        setProduct(product1);
        saveAndAssert(product1);

        Product product2 = new Product();
        setProduct(product2);
        saveAndAssert(product2, 1, 2);

        my_obj.put("productCreatedAt", product1.getProductCreatedAt());
        my_obj.put("productAvailable", "false");
        my_obj.put("productCategory", "Limpeza");
        my_obj.put("productName", "detergente");
        my_obj.put("productBrand", "Ype");
        my_obj.put("productPrice", 6.99);

        mockMvc.perform(put("/api/v1/product/" + product1.getProductId()).contentType(MediaType.APPLICATION_JSON)
                .content(my_obj.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productCreatedAt",
                        Is.is(product1.getProductCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productAvailable", Is.is(false)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productCategory", Is.is("LIMPEZA")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Is.is("detergente")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productBrand", Is.is("Ype")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.productPrice", Is.is(6.99))).andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void itShouldNotUpdateProduct() throws Exception {
        JSONObject my_obj = new JSONObject();

        Product product1 = new Product();
        setProduct(product1);
        saveAndAssert(product1);

        Product product2 = new Product();
        setProduct(product2);
        saveAndAssert(product2, 1, 2);

        my_obj.put("productAvailable", "false");
        my_obj.put("productName", "detergente");
        my_obj.put("productBrand", "Ype");
        my_obj.put("productPrice", 6.99);

        mockMvc.perform(put("/api/v1/product/" + product1.getProductId()).contentType(MediaType.APPLICATION_JSON)
                .content(my_obj.toString())).andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productCategory", Is.is("Invalid category value")))
                .andExpect(status().isBadRequest()).andReturn();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    private void saveAndAssert(Product product) {
        long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from products", Long.class);
        assertEquals(0, countBeforeInsert);
        productService.saveProduct(product);
        long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from products", Long.class);
        assertEquals(1, countAfterInsert);
    }

    private void saveAndAssert(Product product, int before, int after) {
        long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from products", Long.class);
        assertEquals(before, countBeforeInsert);
        productService.saveProduct(product);
        long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from products", Long.class);
        assertEquals(after, countAfterInsert);
    }

    private void setProduct(Product product) {
        product.setProductAvailable(true);
        product.setProductCategory("Banho");
        product.setProductName("Sabonete");
        product.setProductBrand("Nivea");
        product.setProductPrice(1.99);
    }
}
