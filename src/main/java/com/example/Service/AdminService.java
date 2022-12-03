package com.example.Service;

import java.util.List;

import com.example.model.Admin;
import com.example.model.Checker;
import com.example.model.Image;
import com.example.model.User;

public interface AdminService {
	
	public User addUser(User user);
	public Admin addAdmin(Admin admin);
	public Checker addChecker(Checker checker);
	public List<User> getListOfUser();
	public List<Checker> getListOfChecker();
	public List<Admin> getListOfAdmin();
	public boolean EditUser(User user);
	public boolean EditChecker(Checker checker);
	public boolean EditAdmin(Admin Admin);
	public void DeleteUser(String id); // create a get Request
	public void DeleteChecker(String id);
	public void DeleteAdmin(String id);
	public User getUser(User user);
	public Admin getAdmin(Admin admin);
	public Checker getChecker(Checker checker);
	public String getImage(String ImageUri); //create a get Request
	public void setUserImage(Image image);
	public boolean isUniqueUser(User user);
	public boolean isUniqueAdmin(Admin admin);
	public boolean isUniqueChecker(Checker checker);
	public Admin authResult(Admin admin);
	public Admin authentication(String authdata);
	public Admin authwithUnameEmail(Admin obtpass);

	
}
