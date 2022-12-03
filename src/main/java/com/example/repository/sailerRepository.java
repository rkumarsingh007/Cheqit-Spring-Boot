package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Sailer;

@Repository
public interface sailerRepository extends MongoRepository<Sailer,String> {
	
	public Sailer save(Sailer s);
	public Sailer findOneById(String id);
	@Query("{$or:[{email:?0},{rno:?1},{username:?2}]}")
	public List<Sailer> isUnique(String email,int rno,String username);
	public Sailer findOneByUsername(String username);
	
}
