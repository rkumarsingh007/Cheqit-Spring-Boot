package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.UniqueCode;
import com.example.model.User;

@Repository
public interface UniqueCodeRepository extends MongoRepository<UniqueCode , String> {
	
	public UniqueCode findOneByUsername(String username);
	public UniqueCode save(UniqueCode code);
	public UniqueCode insert(User user);
	public void deleteOneByUsername(String username);
}
