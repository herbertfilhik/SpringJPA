package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Adicione métodos personalizados aqui, se necessário

}