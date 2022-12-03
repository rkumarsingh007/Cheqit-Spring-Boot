package com.example.Service;

import java.util.List;
import com.example.model.Checker;
import com.example.model.User;

public interface CheckerService {
	
	public Checker findOneByUsername(String username);
	public Checker findOneByEmail(String email);
	public List<Checker> findAll();
	public Checker authResult	(Checker newuser);
	public Checker authwithUnameEmail(Checker obtpass);
	public String getUserImage(User user);
	public Checker authentication(String authdata);
}
