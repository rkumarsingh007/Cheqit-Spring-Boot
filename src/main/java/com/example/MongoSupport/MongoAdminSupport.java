package com.example.MongoSupport;

import java.io.File;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.example.Security.Config;
import com.example.Service.AdminService;
import com.example.model.Admin;
import com.example.model.Checker;
import com.example.model.Image;
import com.example.model.Main;
import com.example.model.Sailer;
import com.example.model.UniqueCode;
import com.example.model.User;
import com.example.repository.AdminRepository;
import com.example.repository.CheckerRepository;
import com.example.repository.ListRepository;
import com.example.repository.ProductRepo;
import com.example.repository.UniqueCodeRepository;
import com.example.repository.UserRepository;
import com.example.repository.mainRepositery;
import com.example.repository.sailerRepository;

@Service
public class MongoAdminSupport implements AdminService{
	private CheckerRepository checkerRepository;
	private UserRepository userRepository;
	private AdminRepository adminRepository;
	private UniqueCodeRepository uniquecode;
	private ListRepository LstRep;
	private ProductRepo proRepo;
	@Autowired
	MongoTemplate t;
	private static final int VALUE_CALL = 1;
	
	@Autowired
	mainRepositery mainRepo;
	
	@Autowired 
	sailerRepository sailRepo;
	
	@Autowired
	public MongoAdminSupport(CheckerRepository checkerRepository2, UserRepository userRepository2,
			AdminRepository adminRepository2, UniqueCodeRepository uniquecode2, ListRepository listRepository,ProductRepo proRepo) {
		this.checkerRepository =checkerRepository2;
		this.userRepository = userRepository2;
		this.adminRepository = adminRepository2;
		this.uniquecode = uniquecode2;
		this.LstRep = listRepository;
		this.proRepo = proRepo;
	}


	@Override
	public boolean isUniqueUser(User user) {
		
		List<User> u = null;
		u = userRepository.isUnique(user.getEmail(),user.getRno(),user.getUsername());
		if(u.size()>0)
			return false;
		return true;
		
	}

	@Override
	public boolean isUniqueAdmin(Admin admin) {
		List<Admin> u = null;
		u = adminRepository.isUnique(admin.getEmail(),admin.getRno(),admin.getUsername());
		if(u.size()>0)
			return false;
		return true;	
	}

	@Override
	public boolean isUniqueChecker(Checker checker) {
		List<Checker> u = null;
		u = checkerRepository.isUnique(checker.getEmail(),checker.getRno(),checker.getUsername());
		if(u.size()>0)
			return false;
		return true;
	}
	
	public boolean isUniqueSailer(Sailer s) {
		List<Sailer> l = null;
		l = sailRepo.isUnique(s.getEmail(), s.getRno(), s.getUsername());
		if(l.size()>0)
			return false;
		return true;
	}
	@Override
	public User addUser(User user) {
		if(isUniqueUser(user))
		{
			user.setImageURI(Config.COMPANY_PATH+File.separator+user.getName()+File.separator+user.getUsername()+".png");
			UniqueCode code = new UniqueCode();
			code.setUsername(user.getUsername());
			code.setCode("unique");
			uniquecode.insert(code);
			return userRepository.insert(user);
		}
		return null;
	}

	@Override
	public Admin addAdmin(Admin admin) {
		if(isUniqueAdmin(admin))
		{
			admin.setImageURI(Config.ADMIN_PATH+File.separator+admin.getName()+File.separator+admin.getUsername()+".png");
			UniqueCode code = new UniqueCode ();
			code.setUsername(admin.getUsername());
			code.setCode("unique");
			uniquecode.insert(code);
			return adminRepository.insert(admin);
		}
		return null;
	}
	
	public Sailer addSailer(Sailer s) {
		
		if(isUniqueSailer(s)) {
			s.setImageURI(Config.SELLER_PATH+File.separator+s.getName()+File.separator+s.getUsername()+".png");
			UniqueCode code = new UniqueCode();
			code.setUsername(s.getUsername());
			code.setCode("unique");
			uniquecode.insert(code);
			return sailRepo.insert(s);
		}
		return null;
	}

	@Override
	public Checker addChecker(Checker checker) {
		if(isUniqueChecker(checker))
		{
			checker.setImageURI(Config.END_USER_PATH+File.separator+checker.getName()+File.separator+checker.getUsername()+".png");
			UniqueCode code = new UniqueCode ();
			code.setUsername(checker.getUsername());
			code.setCode("unique");
			uniquecode.insert(code);
			return checkerRepository.insert(checker);
		}
		return null;
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

	@Override
	public List<User> getListOfUser() {
		return userRepository.findAllByOrderByRnoAsc();
	}

	@Override
	public List<Checker> getListOfChecker() {
		return checkerRepository.findAllByOrderByRnoAsc();
	}

	@Override
	public List<Admin> getListOfAdmin() {
		return adminRepository.findAllByOrderByRnoAsc();
	}

	@Override
	public boolean EditUser(User user) {
		return userRepository.save(user) != null;
	}

	@Override
	public boolean EditChecker(Checker checker) {
		return checkerRepository.save(checker)!=null;
	}

	@Override
	public boolean EditAdmin(Admin Admin) {
		return adminRepository.save(Admin) != null;
	}

	@Override
	public User getUser(User user) {
		return userRepository.findOneByEmail(user.getEmail());
	}

	@Override
	public Admin getAdmin(Admin admin) {
		return adminRepository.findOneByEmail(admin.getEmail());
	}

	@Override
	public Checker getChecker(Checker checker) {
		return checkerRepository.findOneByEmail(checker.getEmail());
	}

	@Override
	public String getImage(String ImageUri) {
		return UtilBase64Image.encoder(ImageUri);
	}

	//can be used by all   user,checker,admin,sailer
	@Override
	public void setUserImage(Image image)  {
		DeleteImage.deleteIfExist(image.getImageURI());
		UtilBase64Image.decoder(image.getData(), new File(image.getImageURI()).getParent(),image.getUsername()+".png");
	}

	@Override
	public void DeleteUser(String id) {
		User u = new User();
		u = userRepository.findOneById(id);
//		DeleteImage.deleteIfExist(u.getName()+" Products");
//		DeleteImage.deleteIfExist(u.getImageURI());
//		DeleteImage.deleteProductsFile(Config.COMPANY_PATH+u.getName()+" Products");
		DeleteImage.deleteWholeFolder(new File(u.getImageURI()).getParent());
		userRepository.deleteById(id);
		uniquecode.deleteOneByUsername(u.getUsername());
		LstRep.deleteOneByDealerId(u.getId());
		proRepo.deleteAllByDealerId(u.getId());
	}

	@Override
	public void DeleteChecker(String id) {
		Checker u = new Checker();
		u = checkerRepository.findOneById(id);
		DeleteImage.deleteIfExist(u.getImageURI());
		checkerRepository.deleteById(id);
		uniquecode.deleteOneByUsername(u.getUsername());
	}

	@Override
	public void DeleteAdmin(String id) {
		Admin u = new Admin();
		u = adminRepository.findOneById(id);
		DeleteImage.deleteIfExist(u.getImageURI());
		adminRepository.deleteById(id);
		uniquecode.deleteOneByUsername(u.getUsername());
	}

	@Override
	public Admin authResult(Admin admin) {
		Admin a = new Admin();
		a = adminRepository.findOneByUsername(admin.getUsername());
		if(a!=null)
			if(a.getPwd().equals(admin.getPwd()))
			{
				return a;
			}
			else return null;
		return null;
	}


	@Override
	public Admin authentication(String authdata) {
		byte[] decodedBytes = Base64.getDecoder().decode(authdata);
		String decodedString = new String(decodedBytes);
		String[] str = decodedString.split(":");
		Admin a= adminRepository.findOneByUsername(str[0]);
		if(a!=null && a.getPwd().equals(str[1])) {
			return a;
		}
		else return null;
	}


	@Override
	public Admin authwithUnameEmail(Admin obtpass) {
		Admin a = new Admin();
		a = adminRepository.findOneByUsername(obtpass.getUsername());
		if(a.getEmail().equals(obtpass.getEmail())) {
			return a;
		}
		else return null;
	}

}
