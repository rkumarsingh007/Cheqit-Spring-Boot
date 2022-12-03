package com.example.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.MongoSupport.ContactCustomer;
import com.example.exception.SendingMailException;
import com.example.model.ContactUs;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/Identify")
public class ContactUsController {

	@PostMapping(value= "contactUs")
	public boolean sendMail(@RequestBody ContactUs contactDetails) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
					ContactCustomer.sendWelcomeMail(contactDetails);
					ContactCustomer.sendMailToSupport(contactDetails);
					}catch(SendingMailException e) {
						e.getMessage();
					}
				}
			}).start();
			System.out.println("Sent");
		return true;
	}
}
