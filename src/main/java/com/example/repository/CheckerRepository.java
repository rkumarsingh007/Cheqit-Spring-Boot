package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.model.Checker;
import com.example.model.User;

public interface CheckerRepository extends MongoRepository<Checker, String> {

	public Checker findOneByUsername(String username);
	public Checker findOneByEmail(String email);
	public List<Checker> findAllByOrderByRnoAsc();
	public Checker findOneByRno(int rno);
	public void deleteById(String id);
	public Checker findOneById(String id);
	public Checker save(Checker checker);
	public Checker insert(Checker user);
	@Query("{$or:[{email:?0},{rno:?1},{username:?2}]}")
	public List<Checker> isUnique(String email,int rno,String username);
	
	
}