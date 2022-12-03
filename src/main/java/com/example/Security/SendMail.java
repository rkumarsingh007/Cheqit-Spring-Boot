package com.example.Security;

import java.util.Properties;
import java.util.Random;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {


    public static int sendEmail(String to,String sub)
    {
        final String username = "suryabhai.raju";
        final String password = "9216289243";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("suryabhai.raju@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
            message.setSubject(sub);
              Random rnd = new Random();
               int n = 100000 + rnd.nextInt(900000);
            message.setText("hello your code is: ,"
                + "\n\n "+n+"\n Remember Life of Your this code is 5 min.");

            Transport.send(message);

            System.out.println("Done");
               return n;
        } 

        catch (MessagingException e) 
        {
            throw new RuntimeException(e);
            //  System.out.println("Username or Password are incorrect ... exiting !");
        }
    
    }
    synchronized public static int Main(String to,String Sub) 
    {
        return sendEmail(to,Sub);
    }
}