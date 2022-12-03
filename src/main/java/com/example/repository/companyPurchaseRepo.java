package com.example.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.model.CompanyPurchase;

@Repository
public interface companyPurchaseRepo extends MongoRepository<CompanyPurchase,String>{
	public CompanyPurchase save(CompanyPurchase a);
	public CompanyPurchase findOneById(String id);

}
