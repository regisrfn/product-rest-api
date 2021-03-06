package com.rufino.server.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import com.rufino.server.exception.ApiRequestException;
import com.rufino.server.model.Product;
import com.rufino.server.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/product")
@CrossOrigin
public class ProductController {

    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product saveProduct(@Valid @RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PostMapping("savelist")
    public List<Product> saveAllProducts(@Valid @RequestBody List<Product> pList) {
        return productService.saveAllProducts(pList);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public Product getProductById(@PathVariable String id) {
        try {
            UUID productId = UUID.fromString(id);
            Product product = productService.getProductById(productId);
            if (product == null)
                throw new ApiRequestException("Product not found", HttpStatus.NOT_FOUND);
            return product;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid product UUID format", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("{id}")
    public Map<String, String> deleteProductById(@PathVariable String id) {
        Map<String, String> message = new HashMap<>();

        try {
            UUID productId = UUID.fromString(id);
            int op = productService.deleteProductById(productId);
            if (op == 0)
                throw new ApiRequestException("Product not found", HttpStatus.NOT_FOUND);
            message.put("message", "successfully operation");
            return message;
        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid product UUID format", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("{id}")
    public Product updateProduct(@PathVariable String id, @Valid @RequestBody Product product) {
        try {
            UUID productId = UUID.fromString(id);
            product.setProductId(productId);
            return productService.updateProduct(product);

        } catch (IllegalArgumentException e) {
            throw new ApiRequestException("Invalid product UUID format", HttpStatus.BAD_REQUEST);
        }
    }
}
