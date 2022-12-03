package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.model.GenericProduct;

public interface GenericProductRepository extends MongoRepository<GenericProduct,String>{
	public GenericProduct save(GenericProduct d);
	public GenericProduct findOneById(String id);
	public void deleteOneById(String id);

}
