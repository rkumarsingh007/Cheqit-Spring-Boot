package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.tempProduct;

@Repository
public interface tempProRepo extends MongoRepository<tempProduct,String>{
	
	public tempProduct save(tempProduct temp);
	public void deleteById(String id);
	public tempProduct findOneById(String id);
	public List<tempProduct> findAll();

}
