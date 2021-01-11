package com.rufino.server;

import com.rufino.server.model.Product;
import com.rufino.server.service.ProductService;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ServerDeleteTests {
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
    void itShouldDeleteProduct() throws Exception {
        Product product1 = new Product();
        setProduct(product1);
        saveAndAssert(product1);

        Product product2 = new Product();
        setProduct(product2);
        saveAndAssert(product2, 1, 2);

        List<Product> productsList = productService.getAllProducts();
        assertThat(productsList.size()).isEqualTo(2);

        mockMvc.perform(delete("/api/v1/product/" + product1.getProductId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("successfully operation")))
                .andExpect(status().isOk()).andReturn();

        productsList = productService.getAllProducts();
        assertThat(productsList.size()).isEqualTo(1);

    }

    @Test
    void itShouldNotDeleteProduct_NotExists() throws Exception {
        Product product1 = new Product();
        setProduct(product1);
        saveAndAssert(product1);

        Product product2 = new Product();
        setProduct(product2);
        saveAndAssert(product2, 1, 2);

        mockMvc.perform(delete("/api/v1/product/" + "8737659b-3b97-40c0-9529-f0741bba0eeb"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.apiError", Is.is("Product not found")))
                .andExpect(status().isNotFound()).andReturn();

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
