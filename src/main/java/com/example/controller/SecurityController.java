package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.Security.RSAUtil;
import com.google.gson.Gson;

@RestController
@RequestMapping("/Identify")
public class SecurityController {

	@RequestMapping(value="/getKeys",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Map<String,String> getKeys() throws Exception{
		Map<String,String> map = new HashMap();
		map.put("privateKey", RSAUtil.getPrivateKey());
		map.put("publicKey", RSAUtil.getPublicKey());
		return map;
	}

}
