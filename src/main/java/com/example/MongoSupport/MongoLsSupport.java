package com.example.MongoSupport;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.example.Security.EncryptionException;
import com.example.Security.RSAUtil;
import com.example.Service.ListOfBearService;
import com.example.model.AuthProduct;
import com.example.model.ListOfBear;
import com.example.repository.ListRepository;
import com.google.gson.Gson;
import com.mongodb.BasicDBObjectBuilder;

@Service
public class MongoLsSupport implements ListOfBearService{
	ListRepository Lrsp;
	
	@Autowired
	MongoTemplate t ;
	
	@Autowired
	public MongoLsSupport (ListRepository L) {
		this.Lrsp=L;
	}
	
	@Override
	public String[] generateList(String id, int n,AuthProduct pro) throws EncryptionException {
		AuthProduct proList;
		String[] res = new String[n];
		String[] addingDBData = new String[n];
		Random rand = new Random();
		long time = System.currentTimeMillis();
		String stime = String.valueOf(time);
		StringBuilder code  = new StringBuilder("");
		for(int i=0;i<n;i++) {
			int a = rand.nextInt(1000000000) + 1000000;
			proList = new AuthProduct(pro);
			addingDBData[i] = stime+String.valueOf(a)+String.valueOf(i);
			proList.setCode(addingDBData[i]);
			res[i] = RSAUtil.encrypt(new Gson().toJson(proList));
//			System.out.println(res[i]);
		}
		Query q= new Query();
		q.addCriteria(Criteria.where("dealerId").is(id));
		Update update = new Update();
		update.push("list",BasicDBObjectBuilder.start("$each",addingDBData).get());// {$push:{list:{$each:[...]}}}
		t.updateFirst(q, update, ListOfBear.class);
		return res;
	}
	
	/*
	 * 
	 * @Override
	public String[] generateList(String id, int n) {
		String temp[] = new String[n];
		Random rand = new Random();
		long time = System.currentTimeMillis();
		String stime = String.valueOf(time);
		for(int i=0;i<n;i++) {
			int a = rand.nextInt(1000000000) + 1000000;
			temp[i] = stime+String.valueOf(a)+String.valueOf(i);
		}
		Query q= new Query();
		q.addCriteria(Criteria.where("dealerId").is(id));
		Update update = new Update();
		update.push("list",BasicDBObjectBuilder.start("$each",temp).get());// {$push:{list:{$each:[...]}}}
		
		t.updateFirst(q, update, ListOfBear.class);
		
		return temp;
	}
	 */

	@Override
	public boolean checkcode(AuthProduct code) {
		int i= Lrsp.doesItExists(code.getDealerId(), code.getCode());
		if(i>0) 
			return true;
		return false;
	}

	@Override
	public void remove(AuthProduct code) {
		
		Query q= new Query();
		q.addCriteria(Criteria.where("dealerId").is(code.getDealerId()));//  	{$pull:{list:"one element only"}}}
		Update update = new Update();									//		{$pullAll:{list:["one element only"]}}	//hello
		update.pull("list", code.getCode());
		t.updateFirst(q, update, ListOfBear.class);
	}


}
