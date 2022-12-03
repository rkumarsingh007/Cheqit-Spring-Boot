package com.example.MongoSupport;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import com.example.Security.Config;
import com.example.exception.SendingMailException;
import com.example.model.ContactUs;

public class ContactCustomer {

	public static void sendWelcomeMail(ContactUs ContactDetails) throws SendingMailException{
		Properties  props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtpout.secureserver.net");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("info@cheqit.in", "cheqit@1234");
		      }
		   });
		   
		   Message msg = new MimeMessage(session);
		  try {
		   msg.setFrom(new InternetAddress("info@cheqit.in", true));
		   msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(ContactDetails.getEmail()));
		   msg.setSubject("Cheqit Customer Support");
		   if(Config.emailer==null) {
			   Config.emailer = new StringBuilder("");
			   String path = File.separator+"home"+File.separator+"ubuntu"+File.separator+"Cheqit-Server-Spring"+File.separator+"emailer"+File.separator+"index.htm";
			   BufferedReader br = new BufferedReader(new FileReader(new File(path)));
			   String str;
			   while ((str = br.readLine()) != null) 
				    Config.emailer.append(str);
		   } 
		   msg.setContent(Config.emailer.toString(), "text/html");
		   msg.setSentDate(new Date());

//		   MimeBodyPart messageBodyPart = new MimeBodyPart();
//		   messageBodyPart.setContent(ContactDetails.getMessage(), "text/html");
//
//		   Multipart multipart = new MimeMultipart();
//		   multipart.addBodyPart(messageBodyPart);
//		   MimeBodyPart attachPart = new MimeBodyPart();
//
//		   attachPart.attachFile("C:\\Users\\surya\\Desktop\\Cheqit UI material\\cheqitLogo.PNG");
//		   multipart.addBodyPart(attachPart);
//		   msg.setContent(multipart);
		   Transport.send(msg);  
		  }catch(IOException e) {
			  throw new SendingMailException("Try After Sometime",e);
		  } catch (AddressException e) {
			throw new SendingMailException("Invalid Email Address",e);
		  } catch (MessagingException e) {
			  throw new SendingMailException("Message Syntex Error",e);
		  }
	}
	public static  void sendMailToSupport(ContactUs contactDetails) throws SendingMailException {
		Properties  props = new Properties();
		   props.put("mail.smtp.auth", "true");
		   props.put("mail.smtp.starttls.enable", "true");
		   props.put("mail.smtp.host", "smtpout.secureserver.net");
		   props.put("mail.smtp.port", "587");
		   
		   Session session = Session.getInstance(props, new javax.mail.Authenticator() {
		      protected PasswordAuthentication getPasswordAuthentication() {
		         return new PasswordAuthentication("info@cheqit.in", "cheqit@1234");
		      }
		   });
		   
		   Message msg = new MimeMessage(session);
		  try {
		   msg.setFrom(new InternetAddress("info@cheqit.in", true));
		   InternetAddress List[] = {new InternetAddress(
		            "info@cheqit.in"),new InternetAddress("mail.cheqit@gmail.com"),new InternetAddress("suryabhai.raj@gmail.com")};
		   msg.setRecipients(Message.RecipientType.TO, List);
		   msg.setSubject("Message from Cheqit.in");
		   
//		   msg.setContent(Config.emailer.toString(), "text/html");
		   msg.setContent("<h1>Message From : "+contactDetails.getEmail()+"</h1><br><h2>Message:</h2><br>"+contactDetails.getMessage(),"text/html");
		   msg.setSentDate(new Date());

//		   MimeBodyPart messageBodyPart = new MimeBodyPart();
//		   messageBodyPart.setContent(contactDetails.getMessage(), "text/html");
//
//		   Multipart multipart = new MimeMultipart();
//		   multipart.addBodyPart(messageBodyPart);
//		   MimeBodyPart attachPart = new MimeBodyPart();
//
//		   attachPart.attachFile("C:\\Users\\surya\\Desktop\\Cheqit UI material\\cheqitLogo.PNG");
//		   multipart.addBodyPart(attachPart);
//		   msg.setContent(multipart);
		   Transport.send(msg);  
		  } catch (AddressException e) {
			throw new SendingMailException("Invalid Email Address",e);
		  } catch (MessagingException e) {
			  throw new SendingMailException("Message Syntex Error",e);
		  }
	}
}
