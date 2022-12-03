package com.example.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.MongoSupport.MongoLsSupport;
import com.example.MongoSupport.MongoUniqueCodeSupport;
import com.example.MongoSupport.MongoUserSupport;
import com.example.MongoSupport.ProductService;
import com.example.MongoSupport.UtilBase64Image;
import com.example.MongoSupport.tempProService;
import com.example.Security.SendMail;
import com.example.model.Admin;
import com.example.model.AuthProduct;
import com.example.model.CompanyPurchase;
import com.example.model.Products;
import com.example.model.TempUser;
import com.example.model.UniqueCode;
import com.example.model.User;
import com.example.model.temp;
import com.example.model.tempProduct;
import com.example.repository.ListRepository;
import com.example.repository.ProductRepo;
import com.example.repository.UniqueCodeRepository;
import com.example.repository.UserRepository;
import com.example.repository.companyPurchaseRepo;
import com.example.repository.mainRepositery;
import com.google.gson.Gson;
import com.example.repository.tempProRepo;

@RestController
@RequestMapping("/Identify/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ListRepository lRsp;
	@Autowired
	private UniqueCodeRepository uniquecode;
	@Autowired
	private MongoUserSupport mongouser= new MongoUserSupport(userRepository);
	@Autowired
	private MongoUniqueCodeSupport uCodeSupport = new MongoUniqueCodeSupport(uniquecode);
	@Autowired
	private MongoLsSupport LsSupport = new MongoLsSupport(lRsp);
	@Autowired
	ProductRepo proRepo;
	@Autowired
	private tempProRepo tempPRepo;
	@Autowired
	private ProductService proService = new ProductService(proRepo,userRepository);
	
	@Autowired
	private tempProService tempPro = new tempProService(tempPRepo,userRepository,proService);
	@Autowired
	MongoTemplate t;
	@Autowired
	mainRepositery mainRepo;
	@Autowired 
	companyPurchaseRepo cpRepo;
	
	
	final static int VALUE_A = 1;
	
	//get
	@RequestMapping(value="/{id}",method = RequestMethod.GET  ///this can create a problem
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public User get(@PathVariable("id")String id) throws Exception {
//		GroupOperation  op = new 
		return userRepository.findOneById(id);
	}
	
	@RequestMapping(value="/{email}/{rno}/{username}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<User> isUnique(@PathVariable("email")String email,@PathVariable("rno")String rno,@PathVariable("username")String username) throws Exception{
		List<User> user = userRepository.isUnique(email,Integer.parseInt(rno),username);
		return user;
		//return userRepository.aggrigate(u);
	}
	
	//test
	@RequestMapping(value="/runagg",method = RequestMethod.GET  ///this can create a problem
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public temp runagg() {
		temp t = mongouser.runagg();
		return t;
	}
	
	//	checking the unique code
	@RequestMapping(value = "/CheckCode",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public User checkCode (@RequestBody UniqueCode Ucode ) throws Exception {
		User u = new User();
		Ucode = uniquecode.findOneByUsername(Ucode.getUsername());
		if(uCodeSupport.checkCodeAuth(Ucode)) {
			u = userRepository.findOneByUsername(Ucode.getUsername());
			int t= u.isLoggedin()+1;
			u.setLoggedin(t);
			userRepository.save(u);	
			return u;
		}
		else
			return null;
	}
	
	//authentication
	@RequestMapping(value="/auth",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public User auth (@RequestBody User authdata ) throws Exception {
		User temp=null;
		temp = mongouser.authResult(authdata);
		if(temp!=null)
		{
			int log = temp.getLoggedin();
			log++;
			temp.setLoggedin(log);
			userRepository.save(temp);
			return temp;
//			if(temp.isLoggedin()==null || temp.isLoggedin()==0)
//			{
//				temp.setLoggedin(1);
//				userRepository.save(temp);
//				return temp;
//			}
//			else {
//				User u = new User();
//				u.setDetails("1234567890987654321");
//				Integer i = new Integer(0);
//				i = SendMail.Main(temp.getEmail(),"Trying to logging in On Other device..");
//				if(i!=0) {
//				UniqueCode code= new UniqueCode();
//				code= uniquecode.findOneByUsername(temp.getUsername());
//				code.setCode(i.toString());
//				uCodeSupport.saveNewCode(code);
//				}
//				return u;
//			}
		}
		else
			return null;
	}
	
	//update the code
	@RequestMapping(value="/changecode/{code}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UniqueCode updateCode(@PathVariable("code")String code,@RequestHeader("auth")String authdata )throws Exception{
		User c =new User();
		if((c = mongouser.authentication(authdata))!=null)
		{
			UniqueCode ucode = new UniqueCode();
			ucode.setUsername(c.getUsername());
			ucode.setCode(code);
			ucode = uCodeSupport.saveNewCode(ucode);
			return ucode;
		}	
		else return null;
	}
	
//	@RequestMapping(value="/generatemulticode/{n}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//	public String[] generateCode(@PathVariable("n")String n,@RequestHeader("auth")String authdata )throws Exception{
//		User c =new User();
//		int a = Integer.parseInt(n);
//		if((c = mongouser.authentication(authdata))!=null)
//		{
//			String temp[] = LsSupport.generateList(c.getId(), a);
//			return temp;
//		}	
//		else return null;
//	}
	
	@RequestMapping(value="/generateEncMutliTemp/{n}",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String[] generateEcryptedCode(@PathVariable("n")String n,@RequestHeader("auth")String authdata,@RequestBody Map<String,Object> productID )throws Exception{
		User c =new User();
		int a = Integer.parseInt(n);
		AuthProduct pro = new AuthProduct();
		if((c = mongouser.authentication(authdata))!=null)
		{
			pro.setDealerId(c.getId());
			pro.setPid(String.valueOf(productID.get("pid")));
			String temp[] = LsSupport.generateList(c.getId(),a,pro);
			return temp;
		}
		else return null;
	}
	
	//logout
	@RequestMapping(value="/logout",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void logout(@RequestHeader("auth")String authdata )throws Exception{
		User c= new User();
		if((c = mongouser.authentication(authdata))!=null)
		{
			int t=c.isLoggedin();
			if(t>0)
				t--;
			c.setLoggedin(t);
			userRepository.save(c);
		}	
	
	}
	//change password
	@RequestMapping(value="/forgotPassword",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UniqueCode fogrotPassword(@RequestBody User obtpass ) throws Exception  {
		Integer i = new Integer(0);
		User user= null;
		if((user= mongouser.authwithUnameEmail(obtpass))!=null)
		{
			i = SendMail.Main(obtpass.getEmail(),"Password Reset IdentifyMe");
			if(i!=0) {
			UniqueCode code= new UniqueCode();
			code= uniquecode.findOneByUsername(user.getUsername());
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
		u = uniquecode.findOneByUsername(temp.getUsername());
		Date date1 = new Date(System.currentTimeMillis());
		Date date2 = u.getDate();
		long delta = date1.getTime() - date2.getTime();
		if(u.getCode().equals(temp.getCode()) && delta<=300000)
		{
			User user= new User();
			user=userRepository.findOneByUsername(temp.getUsername());
			user.setPwd(temp.getPwd());
			userRepository.save(user);
			return true;
		}
		else return false;
	}
	
	//get own image
		@RequestMapping(value="/GetOwnImage",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public String GetUserImage(@RequestHeader("auth")String authdata) throws Exception {
			User c =new User();
			if((c = mongouser.authentication(authdata))!=null)
			{
				return UtilBase64Image.encoder(c.getImageURI());
			}
			return null;
		}
	
		@RequestMapping(value="/getProductImage/{id}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public String getProductImage(@PathVariable("id") String id,@RequestHeader("auth")String authdata) throws Exception{
			Products p = proRepo.findOneById(id);
//			System.out.println(p.getPname());
			return UtilBase64Image.encoder(p.getImageUri());
		}
		
		@RequestMapping(value="/getMyProductList",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public Products[] getProductList(@RequestHeader("auth")String authdata) throws Exception{
			User u = new User();
			if((u = mongouser.authentication(authdata))!=null)
			{
				Products[] p = proRepo.findAllByDealerId(u.getId());
				return p;
			}
			return null;
		}
		
		@RequestMapping(value="/reqQrMoney/{money}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public String getMoney(@PathVariable("money") String money,@RequestHeader("auth")String authdata) throws Exception{
			User u = new User();
			if(mongouser.authentication(authdata)!=null) {
				//
				 Map<String,Object> json = new HashMap();
				 json.put("cost",money);
				 json.put("ourUPI",mainRepo.findOneByCall(VALUE_A).getOurUPI());
				 return new Gson().toJson(json);
			}
			return null;
		}
		
		@RequestMapping(value="/RegisterPurchase",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public boolean regCompanyPayment(@RequestBody CompanyPurchase purchase,@RequestHeader("auth")String authdata) {
			if(mongouser.authentication(authdata)!=null) {
				cpRepo.save(purchase);
				return true;
			}
			return false;
		}
		
		@RequestMapping(value="/requestAddProduct",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
		public boolean requestAdminforProduct(@RequestBody tempProduct pro,@RequestHeader("auth")String authdata) {
			User u = null;
			if((u = mongouser.authentication(authdata))!=null) {
				if(pro.getDealerId().equals(u.getId())) {
					return  tempPro.addSinglePro(pro);
				}
			}
			return false;
		
		}
		
}
