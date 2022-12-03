package com.example.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.MongoSupport.MongoAdminSupport;
import com.example.MongoSupport.MongoCheckerSupport;
import com.example.MongoSupport.MongoLsSupport;
import com.example.MongoSupport.MongoUniqueCodeSupport;
import com.example.MongoSupport.UtilBase64Image;
import com.example.Security.Config;
import com.example.Security.SendMail;
import com.example.model.AuthProduct;
import com.example.model.CardTrans;
import com.example.model.Checker;
import com.example.model.Products;
import com.example.model.Sailer;
import com.example.model.TempUser;
import com.example.model.Transection;
import com.example.model.UniqueCode;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.CheckerRepository;
import com.example.repository.ListRepository;
import com.example.repository.ProductRepo;
import com.example.repository.TransectionRepo;
import com.example.repository.UniqueCodeRepository;
import com.example.repository.UserRepository;
import com.example.repository.mainRepositery;
import com.example.repository.sailerRepository;
import com.google.gson.Gson;

@RestController
@RequestMapping("/Identify/checker")
public class CheckerController {
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	CheckerRepository checkerRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ListRepository LRsp;
	@Autowired
	private UniqueCodeRepository uniquecode;
	@Autowired 
	mainRepositery mainRepo;
	@Autowired
	private MongoUniqueCodeSupport uCodeSupport = new MongoUniqueCodeSupport(uniquecode);
	@Autowired
	private MongoLsSupport LsSupport = new MongoLsSupport(LRsp);
	@Autowired
	private ProductRepo proRepo;
	@Autowired
	private sailerRepository sailerRepo;
	@Autowired 
	private TransectionRepo transRepo;
	@Autowired
	private MongoAdminSupport mongoadmin = new MongoAdminSupport(checkerRepository,
			userRepository,adminRepository,uniquecode,LRsp,proRepo);
	@Autowired
	private MongoCheckerSupport mongochecker= new MongoCheckerSupport(checkerRepository,userRepository,adminRepository,mainRepo,transRepo);
	private static final int VALUE_A = 1;
	@Autowired
	MongoTemplate t;
	
	//authentication
	@RequestMapping(value="/auth",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Checker auth (@RequestBody Checker authdata ) throws Exception {
//		System.out.println(authdata.getUsername()+" "+authdata.getPwd());
		Checker temp=null;
		if((temp = mongochecker.authResult(authdata))!=null)
		{
			return temp;
		}
		else
			return null;
	}
	
	//data inside the ImageURI
	@RequestMapping(value="/RegisterChecker",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Checker registerChecker(@RequestBody Checker checker) throws Exception{
		checker.setRno(mongochecker.generateReg());
		String imagedata = checker.getImageURI();
		if((checker = mongoadmin.addChecker(checker))!=null) {
			UtilBase64Image.decoder(imagedata,new File(checker.getImageURI()).getParent(), checker.getUsername()+".png");
			return checker;
		}
		mongochecker.goBackReg();
		return null;
	}
	
	
	@RequestMapping(value="/RegisterSealer",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Sailer registerSealer(@RequestBody Sailer s)throws Exception{
		s.setRno(mongochecker.generateReg());
		String imagedata = s.getImageURI();
		if((s = mongoadmin.addSailer(s))!=null) {
			System.out.println("saving the image");
			UtilBase64Image.decoder(imagedata,new File(s.getImageURI()).getParent() , s.getUsername()+".png");
			return s;
		}
		mongochecker.goBackReg();
		return null;
	}

	//called for checking authenticity
	@RequestMapping(value="/GetUserDetails",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String,Object> GetUserDetails(@RequestBody AuthProduct proCode,@RequestHeader("auth")String authdata) throws Exception {
		System.out.println(proCode);
		if(mongochecker.authentication(authdata)!=null || mongochecker.authentication(authdata,"Sailer")!=null)
		{
			
			if(LsSupport.checkcode(proCode))
			{
				Map<String,Object> map = new HashMap();
				map.put("Product_Obj", proRepo.findOneById(proCode.getPid()));
				map.put("Company_Obj",userRepository.findOneById(proCode.getDealerId()));
				//genrate UPI ID:
				
				return map;
				
			}
			else return null;
		}
		else return null;
	}
	
	
	//get the image Product and delete it from the database
	@RequestMapping(value="/GetUserImage",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String GetUserImage(@RequestBody AuthProduct Ucode,@RequestHeader("auth")String authdata) throws Exception {
		System.out.println("getImage");		
		if(mongochecker.authentication(authdata)!=null || mongochecker.authentication(authdata,"Sailer")!=null)
		{
			System.out.println("auth");
			if(LsSupport.checkcode(Ucode))
			{
				User u = new User();
				u.setUsername(Ucode.getDealerId());
				Products pro = proRepo.findOneById(Ucode.getPid());
//				LsSupport.remove(Ucode);
				return UtilBase64Image.encoder(pro.getImageUri());
			}
			else return null;
		}
		else return null;
	}
	
	//servies for both sellers and buyers
	@RequestMapping(value="/GetMyTransection",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<CardTrans> getTransection(@RequestBody Map<String, String> json,@RequestHeader("auth")String authdata){
		if(mongochecker.authentication(authdata,json.get("type"))!=null || mongochecker.authentication(authdata)!=null)
		{
			String type = json.get("type");

			System.out.println("id = "+json.get("id"));
			if(type.equals("Checker")) {
				List<CardTrans> res = mongochecker.myTransBuyer(json.get("id"));
//				System.out.println(res.toString());
				return res;
			}else if(type.equals("Sailer")){
				return mongochecker.myTaransSailer(json.get("id"));
			}
		}
		return null;
	}


	//get own image
	@RequestMapping(value="/GetOwnImage",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String GetUserImage(@RequestHeader("auth")String authdata) throws Exception {
		
		Checker c =new Checker();
		if((c = mongochecker.authentication(authdata))!=null)
		{	
			return UtilBase64Image.encoder(c.getImageURI());
		}
		return null;
	}
	//getTransDetails
	@RequestMapping(value="/getTransDetails/{tid}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getTransDetails(@RequestHeader("auth")String authdata,@PathVariable("tid")String tid) throws Exception {
		if(mongochecker.authentication(authdata)!=null || mongochecker.authentication(authdata,"Sailer")!=null)
		{
			Transection t = transRepo.findOneById(tid);
			Products p = proRepo.findOneById(t.getPid());
			User u = userRepository.findOneById(t.getCid());
			Sailer s= sailerRepo.findOneById(t.getSailerId());
			Map<String,Object> json = new HashMap();
			json.put("success", t.isSuccess());
			json.put("pname", p.getPname());
			json.put("transId", t.getTransId());
			json.put("cname", u.getName());
			json.put("cost", p.getCost());
			json.put("sno", s.getPno());
			json.put("pid",t.getPid());
			json.put("address",t.getAddress());
			return new Gson().toJson(json);
		}else return null;
	}
	
	
	//change Password
	@RequestMapping(value="/forgotPassword",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UniqueCode fogrotPassword(@RequestBody Checker obtpass ) throws Exception  {
		Integer i = new Integer(0);
		Checker checker= null;
		if((checker= mongochecker.authwithUnameEmail(obtpass))!=null)
		{
			i = SendMail.Main(obtpass.getEmail(),"One Time Password by IdentifyMe");
			if(i!=0) {
			UniqueCode code= new UniqueCode();
			code= uniquecode.findOneByUsername(checker.getUsername());
			code.setCode(i.toString());
			uCodeSupport.saveNewCode(code);
			return code;
			}
			return null;
		}
		return null;
	}
	
	//changePassword
	@RequestMapping(value="/changePassword",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean changePassword(@RequestBody TempUser temp) throws Exception {
		UniqueCode u = new UniqueCode();
		u =uniquecode.findOneByUsername(temp.getUsername());
		if(u.getCode().equals(temp.getCode()))
		{
			Checker user= new Checker();
			user=checkerRepository.findOneByUsername(temp.getUsername());
			user.setPwd(temp.getPwd());
			checkerRepository.save(user);
			return true;
		}
		else return false;
	}
	
	//changePassword while Logged in
	@RequestMapping(value="/RegisterTransaction",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean registerTransectinDetails(@RequestBody Transection trans,@RequestHeader("auth")String authdata) throws Exception {
		System.out.println("Transection Called");
		if(mongochecker.authentication(authdata)!=null || mongochecker.authentication(authdata,"Sailer")!=null)
		{
			transRepo.save(trans);
			return true;
		}
		else return false;
	}
	
	
}