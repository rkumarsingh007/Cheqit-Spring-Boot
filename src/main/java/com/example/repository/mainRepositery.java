package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Main;

@Repository
public interface mainRepositery extends MongoRepository<Main, String>{
	public Main findOneByCall(int a);
	public Main save(Main m);

}
