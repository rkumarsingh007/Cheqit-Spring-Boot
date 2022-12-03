package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Products;
import com.example.model.Transection;

@Repository
public interface TransectionRepo extends MongoRepository<Transection, String>{
	public Transection save(Transection trans);
	public List<Transection> findAllByBuyerId(String str);
	public List<Transection> findAllBySailerId(String str);
	public List<Transection> findAllByPid(String str);
	public Transection findOneById(String id);
}
