package com.example.Service;

import java.util.List;
import com.example.model.User;
import com.example.model.temp;

public interface UserService {
	
	public User findOneByUsername(String username);
	public User findOneByEmail(String email);
	public List<User> findAll();
	public User authResult	(User newuser);
	public User authwithUnameEmail(User obtpass);
	public User authentication(String authdata);
	public temp runagg();
	
}
