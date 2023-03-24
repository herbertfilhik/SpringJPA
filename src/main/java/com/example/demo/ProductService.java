package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ProductService {
	
	private static final Logger logger = LogManager.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

	public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void deleteProductById(Long id) {
    	logger.debug("Deleting product with id {}", id);
        productRepository.deleteById(id);
    }
}