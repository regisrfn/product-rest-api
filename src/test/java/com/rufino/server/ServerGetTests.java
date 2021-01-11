package com.rufino.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.model.Product;
import com.rufino.server.model.Product.Category;
import com.rufino.server.service.ProductService;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ServerGetTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProductService productService;

    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void clearTable() {
        jdbcTemplate.update("DELETE FROM PRODUCTS");
    }

    @Test
    void itShouldGetAllProducts() throws Exception {
        JSONObject my_obj = new JSONObject();

        MvcResult result = mockMvc
                .perform(get("/api/v1/product/").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        List<Product> productList = Arrays
                .asList(objectMapper.readValue(result.getResponse().getContentAsString(), Product[].class));

        assertThat(productList.size()).isEqualTo(0);

        Product product1 = new Product();
        setProduct(product1);
        saveAndAssert(product1);

        Product product2 = new Product();
        setProduct(product2);
        saveAndAssert(product2, 1, 2);

        result = mockMvc
                .perform(get("/api/v1/product/").contentType(MediaType.APPLICATION_JSON).content(my_obj.toString()))
                .andExpect(status().isOk()).andReturn();

        productList = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), Product[].class));

        assertThat(productList.size()).isEqualTo(2);
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
		product.setProductCategory(Category.BANHO);
		product.setProductName("Sabonete");
		product.setProductBrand("Nivea");
		product.setProductPrice(1.99);
        }
}
