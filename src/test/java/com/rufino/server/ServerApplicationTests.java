package com.rufino.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import com.rufino.server.model.Product;
import com.rufino.server.model.Product.Category;
import com.rufino.server.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ServerApplicationTests {

	@Autowired
	private ProductService productService;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@BeforeEach
	void clearTable() {
		jdbcTemplate.update("DELETE FROM PRODUCTS");

	}

	//////////////////// SAVE PRODUCT/////////////////////////////////
	@Test
	void itShouldSaveIntoDb() {
		Product product = new Product();
		setProduct(product);
		long countBeforeInsert = jdbcTemplate.queryForObject("select count(*) from products", Long.class);
		assertEquals(0, countBeforeInsert);
		productService.saveProduct(product);
		long countAfterInsert = jdbcTemplate.queryForObject("select count(*) from products", Long.class);
		assertEquals(1, countAfterInsert);
	}

	//////////////////// SAVE All/////////////////////////////////
	@Test
	void itShouldSaveAllIntoDb() {

		List<Product> productsList = productService.getAllProducts();
		assertThat(productsList.size()).isEqualTo(0);

		Product product1 = new Product();
		setProduct(product1);
		Product product2 = new Product();
		setProduct(product2);

		productService.saveAllProducts(List.of(product1,product2));

		productsList = productService.getAllProducts();
		assertThat(productsList.size()).isEqualTo(2);

	}

	//////////////////// GET ALL/////////////////////////////////
	@Test
	void itShouldGetAllProducts() {
		List<Product> productsList = productService.getAllProducts();
		assertThat(productsList.size()).isEqualTo(0);

		Product product1 = new Product();
		setProduct(product1);
		saveAndAssert(product1);

		Product product2 = new Product();
		setProduct(product2);
		saveAndAssert(product2, 1, 2);

		productsList = productService.getAllProducts();
		assertThat(productsList.size()).isEqualTo(2);
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
