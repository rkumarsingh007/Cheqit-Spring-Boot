package com.example.controller;


import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.MongoSupport.MongoAdminSupport;
import com.example.MongoSupport.MongoUniqueCodeSupport;
import com.example.MongoSupport.MongoUserSupport;
import com.example.MongoSupport.ProductService;
import com.example.MongoSupport.UtilBase64Image;
import com.example.MongoSupport.tempProService;
import com.example.Security.SendMail;
import com.example.model.Admin;
import com.example.model.AuthProduct;
import com.example.model.Checker;
import com.example.model.Image;
import com.example.model.ListOfBear;
import com.example.model.Products;
import com.example.model.TempUser;
import com.example.model.UniqueCode;
import com.example.model.User;
import com.example.model.tempProduct;
import com.example.repository.AdminRepository;
import com.example.repository.CheckerRepository;
import com.example.repository.ListRepository;
import com.example.repository.ProductRepo;
import com.example.repository.UniqueCodeRepository;
import com.example.repository.UserRepository;
import com.example.repository.tempProRepo;

@RestController
@RequestMapping("/Identify/admin")
public class AdminController {
	@Autowired
	UniqueCodeRepository uniquecode;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CheckerRepository checkerRepository;
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	ListRepository listRepository;
	@Autowired
	ProductRepo proRepo;
	@Autowired
	private tempProRepo tempPRepo;
	@Autowired
	private MongoAdminSupport mongoadmin = new MongoAdminSupport(checkerRepository,
			userRepository,adminRepository,uniquecode,listRepository,proRepo);
	@Autowired
	private MongoUniqueCodeSupport uCodeSupport = new MongoUniqueCodeSupport(uniquecode); //only for forget password
	@Autowired
	private ProductService proService = new ProductService(proRepo,userRepository);
	
	@Autowired
	private tempProService tempPro = new tempProService(tempPRepo,userRepository,proService);
	
	@Autowired
	private MongoUserSupport mongouser = new MongoUserSupport(userRepository);
	
	
	//admin Login
	@RequestMapping(value="/auth",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Admin auth(@RequestBody Admin authdata) throws Exception{//authdata contains username and password
		Admin temp=null;
		if((temp = mongoadmin.authResult(authdata))!=null)
		{
			return temp;
		}
		else
			return null;
	}
	
	
	//add Admin
	@RequestMapping(value="/AddAdmin",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Admin AddAdmin(@RequestBody Admin admin,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
			admin.setRno(mongoadmin.generateReg());
			if((admin = mongoadmin.addAdmin(admin))!=null) {
				return admin;
			}
			mongoadmin.goBackReg();
			return null;
			
	}
	
	//add User
	@RequestMapping(value="/AddUser",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public User AddUser(@RequestBody User user,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null) {
			user.setRno(mongoadmin.generateReg());
			if((user = mongoadmin.addUser(user))!=null) {
				//user = userRepository.findOneByUsername(user.getUsername());
				ListOfBear lb= new ListOfBear();
				lb.setDealerId(user.getId());
				lb.setList(null);
				listRepository.save(lb);
				return user;
			}
			mongoadmin.goBackReg();
			return null;
		}
		else
			return null;
	}
	
	//add Checker
	@RequestMapping(value="/AddChecker",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Checker AddChecker(@RequestBody Checker checker,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
			checker.setRno(mongoadmin.generateReg());
			if((checker = mongoadmin.addChecker(checker))!=null) {
				return checker;
			}
			mongoadmin.goBackReg();
			return null;
	}
	
	
	@RequestMapping(value="/AddProducts",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean AddProducts(@RequestBody Products prt,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null || mongouser.authentication(authdata)!=null) {	
			System.out.println("authanticated");
			boolean res = proService.addSinglePro(prt);
			System.out.println(res);
			return res;
		}
		else
			return false;
	}
	//list all type of user
	@RequestMapping(value="/ListOfUser",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<User> getListOfUser(@RequestHeader("auth")String authdata)throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
			return  userRepository.findAllByOrderByRnoAsc();
		else
			return null;
	}
	
	//list of all checker
	@RequestMapping(value="/ListOfChecker",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Checker> getListOfChecker(@RequestHeader("auth")String authdata)throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
			return  checkerRepository.findAllByOrderByRnoAsc();
		else
			return null;
	}
	
	//list of all admin
	@RequestMapping(value="/ListOfAdmin",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Admin> getListOfAdmin(@RequestHeader("auth")String authdata)throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
			return  mongoadmin.getListOfAdmin();
		else
			return null;
	}
	
	//edit everyOne
	@RequestMapping(value="/EditUser",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean EditUser(@RequestBody User user,@RequestHeader("auth")String authdata) throws Exception{ //need whole data of user
		if(mongoadmin.authentication(authdata)!=null)
			return mongoadmin.EditUser(user);
		else
			return false;
	}
	
	@RequestMapping(value="/EditChecker",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean EditChecker(@RequestBody Checker checker,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
			return mongoadmin.EditChecker(checker);
		else
			return false;
	}
	
	@RequestMapping(value="/EditAdmin",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean EditAdmin(@RequestBody Admin admin,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
			return mongoadmin.EditAdmin(admin);
		else
			return false;
	}
	
	//delete all type of user
	@RequestMapping(value="/DeleteAdmin/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void DeleteAdmin(@PathVariable("id")String id ,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
		mongoadmin.DeleteAdmin(id);
	}
	
	@RequestMapping(value="/DeleteUser/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void DeleteUser(@PathVariable("id")String id ,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
			mongoadmin.DeleteUser(id);
	}
	
	@RequestMapping(value="/DeleteChecker/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void DeleteChecker(@PathVariable("id")String id ,@RequestHeader("auth")String authdata) throws Exception{
		if(mongoadmin.authentication(authdata)!=null)
		mongoadmin.DeleteChecker(id);
	}
	
	//set the images for the user
	@RequestMapping(value="/SetUserImage",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void SetUserImage(@RequestBody Image image,@RequestHeader("auth")String authdata) {//image must contain all of its feild
		if(mongoadmin.authentication(authdata)!=null) {
			mongoadmin.setUserImage(image);
		}
	}
	
	//get user image
	@RequestMapping(value="/GetUserImage/{username}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String GetUserImage(@PathVariable("username")String username,@RequestHeader("auth")String authdata) throws Exception {
		
		if( mongoadmin.authentication(authdata)!=null)
		{
			User u = new User();
			u = userRepository.findOneByUsername(username);
			return UtilBase64Image.encoder(u.getImageURI());
		}
		return null;
	}
	
	//get checker Image
	@RequestMapping(value="/GetCheckerImage/{username}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String GetCheckerImage(@PathVariable("username")String username,@RequestHeader("auth")String authdata) throws Exception {
	
		if( mongoadmin.authentication(authdata)!=null)
		{
			Checker c = new Checker();
			c = checkerRepository.findOneByUsername(username);
			return UtilBase64Image.encoder(c.getImageURI());
		}
		return null;
	}
	
	//get admin Image
	@RequestMapping(value="/GetAdminImage/{username}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String GetAdminImage(@PathVariable("username")String username,@RequestHeader("auth")String authdata) throws Exception {
	
		if( mongoadmin.authentication(authdata)!=null)
		{
			Admin a= new Admin();
			a = adminRepository.findOneByUsername(username);
			return UtilBase64Image.encoder(a.getImageURI());
		}
		return null;
	}
	
	@RequestMapping(value="/SetCheckerImage",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void SetCheckerImage(@RequestBody Image image,@RequestHeader("auth")String authdata) {
		System.out.println(image.getImageURI());
		if(mongoadmin.authentication(authdata)!=null)
			mongoadmin.setUserImage(image);
	}
	
	@RequestMapping(value="/SetAdminImage",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void SetAdminImage(@RequestBody Image image,@RequestHeader("auth")String authdata) {
		if(mongoadmin.authentication(authdata)!=null)
			mongoadmin.setUserImage(image);
	}
	
	//change password
	@RequestMapping(value="/forgotPassword",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public UniqueCode fogrotPassword(@RequestBody Admin obtpass) throws Exception  {
		Integer i = new Integer(0);
		Admin user= null;
		if((user= mongoadmin.authwithUnameEmail(obtpass))!=null)
		{
			i = SendMail.Main(obtpass.getEmail(),"One Time Password Identify Me");
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
		u =uniquecode.findOneByUsername(temp.getUsername());
		if(u.getCode().equals(temp.getCode()))
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
	public String  getImage(@RequestHeader("auth")String authdata) {
		Admin admin = new Admin();
		if((admin= mongoadmin.authentication(authdata))!=null)
			return UtilBase64Image.encoder(admin.getImageURI());
		else return null;
	}
	
	
	@RequestMapping(value="/authProductReq/{id}/{value}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public boolean authProReq(@RequestHeader("auth")String authdata,@PathVariable("id") String id,@PathVariable("value")String value) {
		Admin admin = null;
		if((admin= mongoadmin.authentication(authdata))!=null) {
			tempProduct pro = tempPRepo.findOneById(id);
			if(value.equals("1")) {
				tempPro.authorizeProduct(pro);
			}else {
				tempPro.unauthProduct(pro);
			}
		}
		return false;
	}
	
	@RequestMapping(value="/listOfUnAuthPro/{id}/{value}",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<tempProduct> listOfUnAuthPro(@RequestHeader("auth")String authdata){
		if(mongoadmin.authentication(authdata)!=null) {
			return tempPRepo.findAll();
		}
		return null;
	}
	
	
	@RequestMapping(value="/temp",method = RequestMethod.GET)
	public List<AuthProduct> temp(){
		ListOfBear b = listRepository.findOneByDealerId("5d947cb2fa2ea41e909a29fa");
		List<String> lis = b.getList();
		List<AuthProduct> pro = new LinkedList();
		for(int i=0;i<lis.size();i++) {
			AuthProduct t = new AuthProduct();
			t.setDealerId(b.getDealerId());
			t.setPid("5d9ac3aecf3a6dd2f0cac0e9");
			t.setCode(lis.get(i));
			pro.add(t);
		}
		return pro;
		
	}
}
