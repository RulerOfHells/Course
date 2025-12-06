package com.rohan.dev.course.service;

public interface EmailService {
	
	void sendMail(String email, String subject, String text);
	void sendVerificationUrl(String name, String email, String verURL, String account);
	
}
