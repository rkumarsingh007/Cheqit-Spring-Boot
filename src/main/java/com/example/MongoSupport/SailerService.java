package com.example.MongoSupport;

import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.model.Sailer;
import com.example.repository.sailerRepository;

@Service
public class SailerService {
	
	@Autowired
	sailerRepository sailRepo;
	
	public Sailer authResult(Sailer newuser) {
		Sailer user = null;
		user = sailRepo.findOneByUsername(newuser.getUsername());
		
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
	
	public Sailer authentication(String authdata) {
		byte[] decodedBytes = Base64.getDecoder().decode(authdata);
		String decodedString = new String(decodedBytes);
		String[] str = decodedString.split(":");
		Sailer a= sailRepo.findOneByUsername(str[0]);
		if(a.getPwd().equals(str[1])) {
			return a;
		}
		else return null;
	}

}
