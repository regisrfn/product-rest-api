package com.rufino.server.service;

import java.util.List;
import java.util.UUID;

import com.rufino.server.dao.ProductDao;
import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private ProductDao productDao;

    @Autowired
    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product saveProduct(Product product) {
        return productDao.insertProduct(product);
    }

    public List<Product> getAllProducts() {
        return productDao.getAll();
    }

    public List<Product> saveAllProducts(List<Product> pList) {
        return productDao.insertAllProducts(pList);
    }

    public Product getProductById(UUID id) {
        return productDao.getProduct(id);
    }

    public int deleteProductById(UUID id) {
        return productDao.deleteProduct(id);
    }

    public Product updateProduct(UUID id, Product product) {
        try {
            product.setProductId(null);
            return productDao.updateProduct(id, product);
        } catch (DataIntegrityViolationException e) {
            throw new ApiRequestException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public Product updateProduct(Product product) {
        return productDao.insertProduct(product);
    }
}
