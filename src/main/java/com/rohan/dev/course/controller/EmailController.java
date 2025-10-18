package com.rohan.dev.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rohan.dev.course.service.CourseService;
import com.rohan.dev.course.service.EmailService;

import jakarta.servlet.http.HttpSession;

@Controller
public class EmailController {

	@Autowired
	private EmailService emailService;
	
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
	public String sendEmail(@RequestParam String email, @RequestParam String name, Model model, HttpSession session) {

		String subject = "Regarding sending emails";
		String text = "Dear "+name+",\n Courses are :\n" + getCoursesAsText() +"Kind Regards,\nThe Owner";
		
		emailService.sendMail(email, subject, text);
		return "emailpage";
	}
}
