package com.rufino.server.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@Table(name = "products")
@JsonInclude(Include.NON_NULL)
public class Product {

    @Id
    private UUID productId;
    
    @NotBlank(message = "Value should not be empty")
    private String productName;

    @NotBlank(message = "Value should not be empty")
    private String productBrand;

    @NotNull(message = "Value should not be empty")
    private Category productCategory;

    private String productDescription;

    @NotNull(message = "Value should not be empty")
    private Double productPrice;

    private String productSize;

    private String productColor;

    @NotNull(message = "Value should not be empty")
    private Boolean productAvailable;

    private ZonedDateTime productCreatedAt;
    
    enum Category {
        LIMPEZA, BANHO, ALIMENTO
    }

    public Product() {
        setProductCreatedAt(ZonedDateTime.now(ZoneId.of("Z")));
        setProductId(UUID.randomUUID());
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Category getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = Category.valueOf(productCategory.toUpperCase());
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductColor() {
        return productColor;
    }

    public void setProductColor(String productColor) {
        this.productColor = productColor;
    }

    public Boolean getProductAvailable() {
        return productAvailable;
    }

    public void setProductAvailable(Boolean productAvailable) {
        this.productAvailable = productAvailable;
    }

    public ZonedDateTime getProductCreatedAt() {
        return productCreatedAt;
    }

    public void setProductCreatedAt(ZonedDateTime productCreatedAt) {
        this.productCreatedAt = productCreatedAt;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    

}
