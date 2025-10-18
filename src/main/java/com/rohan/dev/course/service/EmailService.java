package com.rohan.dev.course.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	private SimpleMailMessage sms;
	
	public EmailService() {
		sms = new SimpleMailMessage();
	}
	
	public void sendMail(String email, String subject, String text) {		
		sms.setTo(email);
		sms.setSubject(subject);
		sms.setText(text);
		
		mailSender.send(sms);
	}
	
	public void sendVerificationUrl(String name, String email, String verURL, String account) {
		//not now
	}
	
}
