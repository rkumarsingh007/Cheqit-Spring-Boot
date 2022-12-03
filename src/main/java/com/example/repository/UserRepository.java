package com.example.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

	public User findOneByUsername(String username);
	public User findOneByEmail(String email);
	public List<User> findAllByOrderByRnoAsc();
	public User findOneByRno(int rno);
	public void  deleteById(String id);
	public User findOneById(String id);
	public User save(User user);
	public User insert(User user);
	
	@Query("{$or:[{email:?0},{rno:?1},{username:?2}]}")
	public List<User> isUnique(String email,int rno,String username);
	
}
