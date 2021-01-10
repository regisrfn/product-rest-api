package com.rufino.server.dao;

import java.util.List;
import java.util.UUID;

import com.rufino.server.model.Product;

public interface ProductDao {
    Product insertProduct(Product product);

    int deleteProduct(UUID id);

    List<Product> getAll();

    Product getProduct(UUID id);

    Product updateProduct(UUID id, Product product);
}
