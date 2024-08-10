package com.rohan.dev.course.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rohan.dev.course.services.CourseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmailController {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private CourseService service;
	
	private String getCoursesAsText() {
		var sb = new StringBuffer();
		for(var course : service.getAllCourses())
			sb.append(course.toString() + "\n");
		return sb.toString();
	}
	
	@GetMapping("/contact")
	public String onContact() {
		return "emailpage";
	}
	
	@GetMapping("/sendEmail")
	public String sendEmail(@RequestParam(required = false, defaultValue = "0") String email, Model model, HttpSession session) {
		var sms = new SimpleMailMessage();
		
		sms.setTo(email);
		sms.setSubject("Regarding sending emails");
		sms.setText("Dear Rohan Tripathy,\n Courses are :\n" + getCoursesAsText() +"Kind Regards,\nThe Owner");
		
		mailSender.send(sms);
		
		model.addAttribute("name", session.getAttribute("Fun.name"));
		return "emailpage";
	}
}
