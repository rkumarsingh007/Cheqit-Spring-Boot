package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.model.Admin;
import com.example.model.User;

@Repository
public interface AdminRepository extends MongoRepository<Admin, String>{
	
	public Admin findOneByUsername(String username);
	public Admin findOneByEmail(String email);
	public List<Admin> findAllByOrderByRnoAsc();
	public Admin findOneByRno(int rno);
	public void deleteById(String id);
	public Admin findOneById(String id);
	public Admin save(Admin admin);
	public Admin insert(Admin user);
	
	@Query("{$or:[{email:?0},{rno:?1},{username:?2}]}")
	public List<Admin> isUnique(String email,int rno,String username);
}
