package com.rufino.server.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rufino.server.dao.JpaDao;
import com.rufino.server.dao.ProductDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Product;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository implements ProductDao {

    private JpaDao jpaDataAccess;
    private JdbcTemplate jdbcTemplate;
    private ObjectMapper om;

    @Autowired
    public ProductRepository(JpaDao jpaDataAccess, JdbcTemplate jdbcTemplate) {
        this.jpaDataAccess = jpaDataAccess;
        this.jdbcTemplate = jdbcTemplate;
        this.om = new ObjectMapper();

    }

    @Override
    public Product insertProduct(Product product) {
        return jpaDataAccess.save(product);
    }

    @Override
    public int deleteProduct(UUID id) {
        try {
            jpaDataAccess.deleteById(id);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Product> getAll() {
        return jpaDataAccess.findAll();
    }

    @Override
    public Product getProduct(UUID id) {
        return jpaDataAccess.findById(id).orElse(null);
    }

    @Override
    public Product updateProduct(UUID id, Product product) {
        String productString;

        try {
            productString = om.writeValueAsString(product);
            String sql = generateSqlUpdate(product, productString);
            int result = jdbcTemplate.update(sql + "where product_id = ?", id);
            return (result > 0 ? getProduct(id) : null);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ApiRequestException(e.getMessage());
        }
    }

    private String generateSqlUpdate(Product product, String productString) throws JSONException {
        String sql = "UPDATE PRODUCTS SET ";
        JSONObject jsonObject = new JSONObject(productString);
        Iterator<String> keys = jsonObject.keys();
        if (!keys.hasNext()) {
            throw new ApiRequestException("No valid data to update");
        }
        while (keys.hasNext()) {
            String key = keys.next();
            if (key.equals("productName") || key.equals("productDescription") || key.equals("productSize") || key.equals("productColor"))
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='" + jsonObject.get(key) + "' ";
            else if (key.equals("productCreatedAt"))
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='" + product.getProductCreatedAt().toString()
                        + "' ";
            else if (key.equals("productId"))
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "='" + product.getProductId().toString()
                        + "' ";
            else
                sql = sql + key.replaceAll("([A-Z])", "_$1").toLowerCase() + "=" + jsonObject.get(key) + " ";

            if (keys.hasNext()) {
                sql = sql + ", ";
            }
        }
        return sql;
    }

    @Override
    public List<Product> insertAllProducts(List<Product> pList) {
        try {
            return jpaDataAccess.saveAll(pList);
        } catch (Exception e) {
            return new ArrayList<>();
        }
        
    }
}
