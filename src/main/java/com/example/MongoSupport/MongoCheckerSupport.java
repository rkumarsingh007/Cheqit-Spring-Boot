package com.example.MongoSupport;

import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import com.example.Service.CheckerService;
import com.example.model.CardTrans;
import com.example.model.Checker;
import com.example.model.Main;
import com.example.model.Sailer;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.CheckerRepository;
import com.example.repository.TransectionRepo;
import com.example.repository.UserRepository;
import com.example.repository.mainRepositery;
import com.example.repository.sailerRepository;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Service
public class MongoCheckerSupport implements CheckerService {
	
	private CheckerRepository checkerRepository;
	private UserRepository userRepository;
	private AdminRepository adminRepository;
	private TransectionRepo transRepo;
	private mainRepositery mainRepo;
	
	@Autowired
	private MongoTemplate t;
	
	@Autowired
	private sailerRepository sailerRepo;
	
	private static final int VALUE_CALL = 1;
	
	
	
	@Autowired
	public MongoCheckerSupport(CheckerRepository checkerRepository,UserRepository userRepository, AdminRepository adminRepository,mainRepositery mainRepo,TransectionRepo transRepo) {
		this.checkerRepository =checkerRepository;
		this.userRepository = userRepository;
		this.adminRepository = adminRepository;
		this.mainRepo = mainRepo;
		this.transRepo = transRepo;
	}

	@Override
	public Checker findOneByUsername(String username) {
		return checkerRepository.findOneByUsername(username);
	}

	@Override
	public Checker findOneByEmail(String email) {
		// TODO Auto-generated method stub
		return checkerRepository.findOneByEmail(email);
	}
	
	public int generateReg() {
		Main m = mainRepo.findOneByCall(VALUE_CALL);
		int res = m.getRno();
		m.setRno(res+1);
		mainRepo.save(m);
		return res;
	}
	
	public void goBackReg() {
		Main m = mainRepo.findOneByCall(VALUE_CALL);
		int res = m.getRno();
		m.setRno(res-1);
		mainRepo.save(m);
	}
	
	
	public int generateTransId() {
		Main m = mainRepo.findOneByCall(VALUE_CALL);
		int res = m.getTransId();
		m.setTransId(res+1);
		mainRepo.save(m);
		return res;
	}

	public void goBackTrans() {
		Main m = mainRepo.findOneByCall(VALUE_CALL);
		int res = m.getTransId();
		m.setTransId(res-1);
		mainRepo.save(m);
	}
	
	
//	[ { $match: { buyerId: "5d9480f7fa2efa5b865f758e" } },{ "$addFields": { "proId": { "$toObjectId": "$pid" } } },
	//{ $lookup: { from: "Products", localField: "proId", foreignField:"_id" , as: "pro" } },
	//{$unwind:"$pro"} ,{$project:{_id:1,pid:1,type:1,success:1,timeStamp:1,pname:"$pro.pname",cost:"$pro.cost"}} ]4
	
//	{ "$match" : { "buyerId" : "5bc6e9dd0f6d2b047fda3d20"}} , 
//	{ "$addFields" : { "proId" : { "$toObjectId" : "$pid"}}} ,
	//{ "$lookup" : { "from" : "Products" , "localField" : "proId" , "foreignField" : "_id" , "as" : "pro"}} ,
	//{ "$unwind" : "$pro"} ,
	//{ "$project" : { "_id" : 1 , "pid" : 1 , "type" : 1 , "success" : 1 , "timeStamp" : 1 , "pname" : "$pro.pname" , "cost" : "$pro.cost"}}]}
	public List<CardTrans> myTransBuyer(String id){
		List<CardTrans> res = new LinkedList();
		Aggregation aggregation = Aggregation.newAggregation(
			    Aggregation.match(
			        Criteria.where("buyerId").is(id)
			    ),
			    new CustomAggregationOperation(
			            new Document(
			            		"$addFields",new BasicDBObject("proId",new BasicDBObject("$toObjectId","$pid")))),
			    Aggregation.lookup("Products", "proId", "_id", "pro"),
			    Aggregation.unwind("pro"),
//			    Aggregation.project("_id","pid","type","success","timeStamp","pro.pname","pro.cost")
			    new CustomAggregationOperation(
			            new Document(
			            		"$project",new BasicDBObject(new BasicDBObject("_id",new BasicDBObject("$toString","$_id")).append("pid", 1)
			            				.append("type", 1).append("success", 1).append("timeStamp", 1).append("pname","$pro.pname").append("cost","$pro.cost")))
			            )
			).withOptions(new AggregationOptions(false,false, new Document().append("batchSize",Integer.MAX_VALUE)));
//				.withOptions(new AggregationOptions(false,false,new BasicDBObject().append("batchSize" , Integer.MAX_VALUE)));
		AggregationResults<CardTrans> a = t.aggregate(aggregation,"Transection", CardTrans.class);
		System.out.println(a.toString());
		Document sd = a.getRawResults();
		
		DBObject o = (DBObject)sd.get("cursor");
		String s = o.get("firstBatch").toString();
		res = Arrays.asList(new Gson().fromJson(s, CardTrans[].class));
//		JSONArray arr = new JSONArray(s);
//		for (int i = 0; i < arr.length(); i++) {
//            CardTrans u = new Gson().fromJson(arr.getJSONObject(i).toString(),CardTrans.class);
//            res.add(u);
//        }
		return res;
		
	}
	 class CustomAggregationOperation implements AggregationOperation {
	    private Document operation;

	    public CustomAggregationOperation (Document operation) {
	        this.operation = operation;
	    }

		@Override
		public Document toDocument(AggregationOperationContext context) {
			return context.getMappedObject(operation);
		}


//		@Override
//		public DBObject toDBObject(AggregationOperationContext context) {
//			// TODO Auto-generated method stub
//			return context.getMappedObject(operation);
//		}
	}
	
	public List<CardTrans> myTaransSailer(String id) {
		// TODO Auto-generated method stub
		List<CardTrans> res = new LinkedList();
		Aggregation aggregation = Aggregation.newAggregation(
			    Aggregation.match(
			        Criteria.where("sailerId").is(id)
			    ),
			    new CustomAggregationOperation(
			            new Document(
			            		"$addFields",new BasicDBObject("proId",new BasicDBObject("$toObjectId","$pid")))),
			    Aggregation.lookup("Products", "proId", "_id", "pro"),
			    Aggregation.unwind("pro"),
//			    Aggregation.project("_id","pid","type","success","timeStamp","pro.pname","pro.cost")
			    new CustomAggregationOperation(
			            new Document(
			            		"$project",new BasicDBObject(new BasicDBObject("_id",new BasicDBObject("$toString","$_id")).append("pid", 1)
			            				.append("type", 1).append("success", 1).append("timeStamp", 1).append("pname","$pro.pname").append("cost","$pro.cost")))
			            )
			).withOptions(new AggregationOptions(false,false,new Document().append("batchSize" , Integer.MAX_VALUE)));
		System.out.println("ag = "+aggregation);
		AggregationResults<CardTrans> a = t.aggregate(aggregation,"Transection", CardTrans.class);
		System.out.println("agg command = "+a);
		Document sd = a.getRawResults();
		System.out.println("sd = "+sd);
		DBObject o = (DBObject)sd.get("cursor");
		System.out.println("object = "+o);
		String s = o.get("firstBatch").toString();
		res = Arrays.asList(new Gson().fromJson(s, CardTrans[].class));
//		JSONArray arr = new JSONArray(s);
//		for (int i = 0; i < arr.length(); i++) {
//            CardTrans u = new Gson().fromJson(arr.getJSONObject(i).toString(),CardTrans.class);
//            res.add(u);
//        }
		return res;
	}
	@Override
	public List<Checker> findAll() {
		// TODO Auto-generated method stub
		return checkerRepository.findAll();
	}

	@Override
	public Checker authResult(Checker newuser) {
		Checker user = null;
		user = checkerRepository.findOneByUsername(newuser.getUsername());
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
	public Checker authwithUnameEmail(Checker obtpass) {
		Checker user = null;
		user = checkerRepository.findOneByUsername(obtpass.getUsername());
		if(user.getEmail().equals(obtpass.getEmail())) {
			return user;
		}
		else return null;
	}

	@Override
	public String getUserImage(User user) {//need username to get the image
		user = userRepository.findOneByUsername(user.getUsername());
		return UtilBase64Image.encoder(user.getImageURI());
	}

	@Override
	public Checker authentication(String authdata) {
		byte[] decodedBytes = Base64.getDecoder().decode(authdata);
		String decodedString = new String(decodedBytes);
		String[] str = decodedString.split(":");
		Checker a= checkerRepository.findOneByUsername(str[0]);
		if(a!=null && a.getPwd().equals(str[1])) {
			return a;
		}
		else return null;
	}
	
	public Sailer authentication(String authdata,String type) {
		if(type.equals("Sailer")) {
			byte[] decodedBytes = Base64.getDecoder().decode(authdata);
			String decodedString = new String(decodedBytes);
			String[] str = decodedString.split(":");
			Sailer a= sailerRepo.findOneByUsername(str[0]);
			if(a!=null && a.getPwd().equals(str[1])) {
				return a;
			}else return null;
		}
		return null;
	}

	


	

}
