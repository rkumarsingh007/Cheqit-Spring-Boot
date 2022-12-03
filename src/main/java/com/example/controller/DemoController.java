package com.example.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.MongoSupport.DemoService;
import com.example.exception.CompanyDoesNotExistsException;
import com.example.model.Certificate;
import com.example.model.Demo;
import com.example.model.DemoBharatrath;
import com.example.model.GenericProduct;
import com.example.repository.BharatRathRepository;
import com.example.repository.DemoRepo;
import com.google.gson.Gson;
import com.itextpdf.text.DocumentException;

import ch.qos.logback.core.net.SyslogOutputStream;	

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Identify/web-checker")
public class DemoController {
	
	@Autowired
	private DemoRepo demoRep;
	@Autowired
	private DemoService demoServ;
	
	int x =0;
	
	@RequestMapping(value="/genDemoQR",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String getDemoQR(@RequestBody Demo data){
		String enc = demoServ.generateEncyData(data);
		//generate the QR codes
		
		String base64String = demoServ.getQRBase64(enc);
//		printTheQRLocely(base64String);
		return base64String;
		
	}
	
	
	
	private void printTheQRLocely(String base64) {
		
		try {
			byte[] b = Base64.getDecoder().decode(base64);
			ByteArrayInputStream bis = new ByteArrayInputStream(b);
			BufferedImage image = ImageIO.read(bis);
			bis.close();	
			
			File outputfile = new File("C:\\Users\\surya\\Desktop\\1\\"+x+".png");
			x++;
			if(!outputfile.exists())
				outputfile.mkdir();
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//verifing using the object id   {class:"name_of_company",object:"{_id: "mndfbng5865"}}
	@RequestMapping(value="/validateDemoQR",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)		
	public Map<String , Object> validateDemo( @RequestBody Map<String,Object> map) {
		//validate via ID	
		String className = map.get("class").toString();
		switch(className) {
			case "Demo":
				Demo d = new Gson().fromJson(new Gson().toJson(map.get("object")), Demo.class);
//				Demo d = (Demo)obj;
				if((d = demoServ.validateDemo(d))!=null){
					HashMap<String,Object> _map = new HashMap();
					_map.put("class", "Demo");
					_map.put("Demo_Obj", d);
					return _map;
				}
				break;
			case "Certificate":
				Certificate cert = new Gson().fromJson(new Gson().toJson(map.get("object")), Certificate.class);
				if((cert = demoServ.validateCertificate(cert))!=null){
					HashMap<String,Object> _map = new HashMap();
					_map.put("class", "Certificate");
					_map.put("Certificate_Obj", cert);
					return _map;
				}
				break;
			case "BharatRath":
				DemoBharatrath br = new Gson().fromJson(new Gson().toJson(map.get("object")), DemoBharatrath.class);
				if((br = demoServ.validateBharatRath(br))!=null) {
					HashMap<String,Object> _map = new HashMap();
					_map.put("class", "BharatRath");
					_map.put("BharatRath_Obj", br);
					return _map;
				}
				break;
			default:
				GenericProduct gen = new Gson().fromJson(new Gson().toJson(map.get("object")), GenericProduct.class);
				if((gen = demoServ.validateGenericProduct(gen))!=null) {
					HashMap<String,Object> _map = new HashMap();
					_map.put("class", "GenericProduct");
					_map.put("GenericProduct_Obj", gen);
					return _map;
				}
				break;
		}
		return null;
	}
	
	@RequestMapping(value = "/genTagBharatRath/",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String generateTag(@RequestBody DemoBharatrath demo) {
		String base64 = demoServ.generateBharathRathQR(demo);
		printTheQRLocely(base64);
		return base64;
	}
	
	
	@RequestMapping(value = "/genericTag/{companyName}",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, 
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String generateTag(@RequestBody GenericProduct demo,@PathVariable String companyName ) {
		String base64 = demoServ.generateGenericProductQR(demo,companyName);
		printTheQRLocely(base64);
		return base64;
	}
	
	
	@GetMapping(value = "/getComLogoProImage/{proType}")
	public Map<String,String> getComLogoProImage(@PathVariable String proType){
		Map<String,String> map = new HashMap();
//		create base 64 Image and add it to the map
		try {
			map = demoServ.getComImageData(proType);
			return map;
		} catch (CompanyDoesNotExistsException e) {
			return null;
		}
	}
	
	@GetMapping(value = "/getCompanyInfo/{compName}")
	public Map<String,String> getCompanyRelatedData(@PathVariable String compName){
		Map<String,String> map = new HashMap();
		try {
//			System.out.println("compName: "+compName);
			map = demoServ.getCompanyData(compName);
			return map;
		} catch (CompanyDoesNotExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	//decrypt the data and generate the QR code 
	@RequestMapping(value = "/generatePDF",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = "application/pdf")
	public ResponseEntity<byte[]> generatePDFDoc(@RequestBody Certificate cert) {
		cert = demoServ.saveCertData(cert);
		byte[] contents;
		try {
			contents = demoServ.generatePDF(cert);
			HttpHeaders headers = new HttpHeaders();	
		    headers.setContentType(MediaType.APPLICATION_PDF);
		    String filename = cert.getBusinessName()+".pdf";
		    headers.setContentDispositionFormData("attachment", filename);
//		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		    ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(
		            contents, headers, HttpStatus.OK);
		    return response;
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
