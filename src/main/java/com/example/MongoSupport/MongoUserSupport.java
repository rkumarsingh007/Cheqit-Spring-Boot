package com.example.MongoSupport;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Service;

import com.example.Service.UserService;
import com.example.model.User;
import com.example.model.temp;
import com.example.repository.UserRepository;

@Service
public class MongoUserSupport implements UserService {
	
	private UserRepository userRepository;
	
	@Autowired
	MongoTemplate t;
	
	@Autowired
	public MongoUserSupport(UserRepository userRepository) {
		this.userRepository =userRepository;
	}
	
	
	public User authResult(User newuser) {
		
		User user = null;
		user = userRepository.findOneByUsername(newuser.getUsername());
		if(user!=null) {
			if(user.getPwd().equals(newuser.getPwd()))
			{
				return user;
			}
			else return null;
		}
		else
			return null;

	}
	
	
	@Override
	public User findOneByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public User findOneByEmail(String email) {
		
		return userRepository.findOneByEmail(email);
	}
	
	@Override
	public List<User> findAll() {
		
		return userRepository.findAll();
	}

	@Override
	public User authwithUnameEmail(User obtpass) {
		User user = new User();
		user = userRepository.findOneByUsername(obtpass.getUsername());
		if(user.getEmail().equals(obtpass.getEmail())) {
			return user;
		}
		else return null;
	}

 
	@Override
	public User authentication(String authdata)  {
		byte[] decodedBytes = Base64.getDecoder().decode(authdata);
		String decodedString = new String(decodedBytes);
		String[] str = decodedString.split(":");
		User a= userRepository.findOneByUsername(str[0]);
		if(a.getPwd().equals(str[1])) {
			return a;
		}
		else return null;
	}


	//db.user.aggregate([{$group:{_id:"$_id",name:{$first:"$username"},rno:{$max:"$rno"}}},{$project:{_id:0,name:1,rno:1}}]);
	@Override
	public temp runagg() {
		// TODO Auto-generated method stub										
		GroupOperation g = 	Aggregation.group("$user").first("user").as("name").max("age").as("age");
		ProjectionOperation p= Aggregation.project("name","age");
		Aggregation aggregation = Aggregation.newAggregation( g, p);
		AggregationResults<temp> result = t.aggregate(aggregation, "test", temp.class);
		temp element = result.getUniqueMappedResult();
		return element;
	}
	
	
}
