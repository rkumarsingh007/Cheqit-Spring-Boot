package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.MongoSupport.SailerService;
import com.example.MongoSupport.UtilBase64Image;
import com.example.model.Checker;
import com.example.model.Sailer;
import com.example.repository.sailerRepository;

@RestController
@RequestMapping("/Identify/sailer")
public class sailerController {
	
	@Autowired
	sailerRepository sailRepo;
	
	@Autowired
	SailerService sailSupp;
	
	@RequestMapping(value="/auth",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE
			,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Sailer auth (@RequestBody Sailer authdata ) throws Exception {
		Sailer temp=null;
		if((temp = sailSupp.authResult(authdata))!=null)
		{
			System.out.println("done /auth");
			return temp;
		}
		else
			return null;
	}
	
	@RequestMapping(value="/GetOwnImage",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public String GetUserImage(@RequestHeader("auth")String authdata) throws Exception {
		Sailer c =new Sailer();
		if((c = sailSupp.authentication(authdata))!=null)
		{	
			System.out.println("done authanensdngasdg");
			return UtilBase64Image.encoder(c.getImageURI());
		}
		return null;
	}
	
	@RequestMapping(value="/GiveQr",method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Sailer returnQr(@RequestHeader("auth")String authdata) throws Exception {
		Sailer c =new Sailer();
		if((c = sailSupp.authentication(authdata))!=null)
		{	
			return c;
		}
		return null;
	}
	
	
}
