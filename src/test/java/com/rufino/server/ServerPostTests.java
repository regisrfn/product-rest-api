package com.rufino.server;

import org.json.JSONArray;
import org.json.JSONException;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.core.Is;

@SpringBootTest
@AutoConfigureMockMvc
public class ServerPostTests {

        @Autowired
        private JdbcTemplate jdbcTemplate;
        @Autowired
        private MockMvc mockMvc;

        @BeforeEach
        void clearTable() {
                jdbcTemplate.update("DELETE FROM PRODUCTS");
        }

        @Test
        void itShouldSaveProduct() throws Exception {
                JSONObject my_obj = new JSONObject();

                my_obj.put("productAvailable", "true");
                my_obj.put("productCategory", "Limpeza");
                my_obj.put("productName", "detergente");
                my_obj.put("productBrand", "Ype");
                my_obj.put("productPrice", 6.99);

                mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productPrice", Is.is(6.99)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productCategory", Is.is("LIMPEZA")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Is.is("detergente")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productBrand", Is.is("Ype")))
                                .andExpect(status().isOk()).andReturn();

        }

        @Test
        void itShouldSaveProduct_withId() throws Exception {
                JSONObject my_obj = new JSONObject();

                my_obj.put("productAvailable", "true");
                my_obj.put("productCategory", "Limpeza");
                my_obj.put("productName", "detergente");
                my_obj.put("productBrand", "Ype");
                my_obj.put("productId", "40d067ae-0fbd-4bb8-b306-65ac40737aaa");
                my_obj.put("productPrice", 6.99);

                mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productPrice", Is.is(6.99)))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productCategory", Is.is("LIMPEZA")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productName", Is.is("detergente")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productBrand", Is.is("Ype")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.productId",
                                                Is.is("40d067ae-0fbd-4bb8-b306-65ac40737aaa")))
                                .andExpect(status().isOk()).andReturn();
        }

        @Test
        void itShouldNotSaveProduct() throws Exception {
                JSONObject my_obj = new JSONObject();
                my_obj.put("productAvailable", "true");
                my_obj.put("productCategory", "Limpeza");
                my_obj.put("productName", "detergente");
                my_obj.put("productBrand", "Ype");

                mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productPrice",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                .andExpect(status().isBadRequest()).andReturn();

                my_obj.put("productPrice", "  ");
                mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productPrice",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                .andExpect(status().isBadRequest()).andReturn();

                my_obj.put("productPrice", null);
                mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productPrice",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                .andExpect(status().isBadRequest()).andReturn();

                ///////////////////////////////////////////////////////////////////////////////
                my_obj = new JSONObject();
                mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                                .content(my_obj.toString()))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productAvailable",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productCategory",
                                                Is.is("Invalid category value")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productName",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productBrand",
                                                Is.is("Value should not be empty")))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                .andExpect(status().isBadRequest()).andReturn();

        }

        @Test
        void itShouldNotSaveProduct_invalidCategory() {
                JSONObject my_obj = new JSONObject();
                try {
                        my_obj.put("productAvailable", "true");
                        my_obj.put("productCategory", "Higiene");
                        my_obj.put("productName", "detergente");
                        my_obj.put("productBrand", "Ype");
                        my_obj.put("productPrice", 6.99);

                        mockMvc.perform(post("/api/v1/product").contentType(MediaType.APPLICATION_JSON)
                                        .content(my_obj.toString()))
                                        .andExpect(MockMvcResultMatchers.jsonPath("$.message", Is.is("Not OK")))
                                        .andExpect(MockMvcResultMatchers.jsonPath("$.errors.productCategory",
                                                        Is.is("Invalid category value")))
                                        .andExpect(status().isBadRequest()).andReturn();
                } catch (JSONException e) {
                        assert (false);
                        e.printStackTrace();
                } catch (Exception e) {
                        assert (false);
                        e.printStackTrace();
                }

        }

        @Test
        void itShouldSaveProductList() throws Exception {
                JSONArray productList = new JSONArray();
                JSONObject my_obj = new JSONObject();

                my_obj.put("productAvailable", "true");
                my_obj.put("productCategory", "Limpeza");
                my_obj.put("productName", "Detergente");
                my_obj.put("productBrand", "Ype");
                my_obj.put("productPrice", 6.99);

                productList.put(my_obj);

                my_obj = new JSONObject();
                my_obj.put("productAvailable", "true");
                my_obj.put("productCategory", "Banho");
                my_obj.put("productName", "Sabonete");
                my_obj.put("productBrand", "Nivea");
                my_obj.put("productPrice", 1.99);

                productList.put(my_obj);

                my_obj = new JSONObject();
                my_obj.put("productAvailable", "true");
                my_obj.put("productCategory", "Alimento");
                my_obj.put("productName", "Arroz");
                my_obj.put("productBrand", "Blue Bom");
                my_obj.put("productPrice", 30.99);

                productList.put(my_obj);

                mockMvc.perform(post("/api/v1/product/savelist").contentType(MediaType.APPLICATION_JSON)
                                .content(productList.toString())).andExpect(status().isOk()).andReturn();

        }

        @Test
        void itShouldNotSaveProductList() throws Exception {
                JSONArray productList = new JSONArray();
                JSONObject my_obj = new JSONObject();

                my_obj.put("productAvailable", "true");
                my_obj.put("productCategory", "Limpeza");
                my_obj.put("productName", "detergente");
                my_obj.put("productBrand", "Ype");
                my_obj.put("productPrice", 6.99);

                productList.put(my_obj);

                my_obj = new JSONObject();
                my_obj.put("productAvailable", "true");
                my_obj.put("productCategory", "Banho");
                my_obj.put("productName", "sabonete");
                my_obj.put("productPrice", 1.99);

                productList.put(my_obj);

                mockMvc.perform(post("/api/v1/product/savelist").contentType(MediaType.APPLICATION_JSON)
                                .content(productList.toString())).andExpect(status().isOk()).andReturn();

        }

}
