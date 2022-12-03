package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Demo;

//@Repository
public interface DemoRepo extends MongoRepository<Demo,String>{
//	public Demo save(Demo d);
	public Demo findOneById(String id);
}
