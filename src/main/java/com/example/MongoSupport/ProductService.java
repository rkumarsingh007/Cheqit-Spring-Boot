package com.example.MongoSupport;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Security.Config;
import com.example.model.Products;
import com.example.model.User;
import com.example.repository.ProductRepo;
import com.example.repository.UserRepository;

@Service
public class ProductService {
	ProductRepo proRepo;
	UserRepository userRepo;
	@Autowired
	public ProductService(ProductRepo pro,UserRepository userRepo) {
		this.proRepo = pro;	
		this.userRepo = userRepo;
		
	}
	
	public boolean addSinglePro(Products pro) {
		User u = userRepo.findOneById(pro.getDealerId());
		String path = Config.COMPANY_PATH+File.separator+u.getName()+File.separator+ "Products";//"\\"+pro.getPname()+".png";
		UtilBase64Image.decoder(pro.getImageUri(),path,pro.getPname()+".png");
		pro.setImageUri(path+File.separator+pro.getPname()+".png");
		pro.setId(null);
		proRepo.save(pro);
		return true;
	}
	
	
	//currently not in user     ***********************************************************
	private boolean addPro(Products pro[]) {
		int n = pro.length;
		System.out.println("n = "+n);
		for(int i=0;i<n;i++) {
			String path = "D:\\Identify\\"+pro[i].getDealerId();//+"\\"+pro[i].getPname()+".png";
			UtilBase64Image.decoder(pro[i].getImageUri(),path,pro[i].getPname()+".png");
			pro[i].setImageUri(path+File.separator+pro[i].getPname()+".png");
			proRepo.save(pro[i]);
		}
		System.out.println("added");
		return true;
	}
	//*************************************************************************************
	
//	public boolean addSingleTempPro(Products pro) {
//		User u = userRepo.findOneById(pro.getDealerId());
//		String path = "D:\\IdentifyMe\\"+u.getName()+ " Products";//"\\"+pro.getPname()+".png";
////		UtilBase64Image.decoder(pro.getImageUri(),path,pro.getPname()+".png");
//		
//		pro.setImageUri(path+File.separator+pro.getPname()+".png");
//		pro.setId(null);
//		proRepo.save(pro);
//		return true;
//	}

}
