package com.example.MongoSupport;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Products;
import com.example.model.User;
import com.example.model.tempProduct;
import com.example.repository.UserRepository;
import com.example.repository.tempProRepo;
import com.google.gson.Gson;

@Service
public class tempProService {
	tempProRepo tempproRepo;
	UserRepository userRepo;
	ProductService proService;
	
	@Autowired
	public tempProService(tempProRepo proRepo2,UserRepository userRepo,ProductService service) {
		this.tempproRepo = proRepo2;	
		this.userRepo = userRepo;
		this.proService = service;
	}
	
	public boolean addSinglePro(tempProduct pro) { //image uri have image data
		User u = userRepo.findOneById(pro.getDealerId());
		String path = "D:\\IdentifyMe\\"+pro.getPname()+" Temp Products";//"\\"+pro.getPname()+".png";
		UtilBase64Image.decoder(pro.getImageUri(),path,pro.getPname()+".png");
		pro.setImageUri(path+File.separator+pro.getPname()+".png");
		tempproRepo.save(pro);
		return true;
	}
	
	public boolean authorizeProduct(tempProduct pro) { //image uri does not have image data 
		String temp = new Gson().toJson(pro);
		Products p = new Gson().fromJson(temp, Products.class);
		User u = userRepo.findOneById(pro.getDealerId());
		String old_path = pro.getImageUri();
		String new_path = "D:\\IdentifyMe\\"+u.getName()+ " Products";
		try {
			Path pat = Files.move 
			        (Paths.get(old_path),  
			        Paths.get(new_path));
			proService.addSinglePro(p);
			tempproRepo.deleteById(pro.getId());
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return false;
		
	}
	
	public boolean unauthProduct(tempProduct pro) { //image uri does not have image data 
		try {
			DeleteImage.deleteIfExist(pro.getImageUri());
			tempproRepo.deleteById(pro.getId());
			return true;
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
