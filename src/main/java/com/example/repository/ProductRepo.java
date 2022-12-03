package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Admin;
import com.example.model.Products;

@Repository
public interface ProductRepo extends MongoRepository<Products, String>{
	public Products save(Products pro); 
	public Products findOneById(String id);
	@Query(value="{dealerId:?0}")
	public Products[] findAllByDealerId(String id);
	public void deleteAllByDealerId(String username);
}
