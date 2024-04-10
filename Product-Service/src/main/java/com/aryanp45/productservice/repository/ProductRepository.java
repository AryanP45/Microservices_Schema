package com.aryanp45.productservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.aryanp45.productservice.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
