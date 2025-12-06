package com.rohan.dev.course.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	private SimpleMailMessage sms;

	public EmailServiceImpl() {
		sms = new SimpleMailMessage();
	}

	@Override
	public void sendMail(String email, String subject, String text) {
		sms.setTo(email);
		sms.setSubject(subject);
		sms.setText(text);

		mailSender.send(sms);
	}

	@Override
	public void sendVerificationUrl(String name, String email, String verURL, String account) {
		// not now
	}
}
