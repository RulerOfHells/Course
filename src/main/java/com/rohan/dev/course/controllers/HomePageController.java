package com.rohan.dev.course.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rohan.dev.course.services.CourseService;
import com.rohan.dev.course.services.RegistrationService;

@Controller
public class HomePageController {
	
	private CourseService courseService;
	
	private RegistrationService registrationService;
	
	@Autowired
	public HomePageController(CourseService courseService, RegistrationService registrationService) {
		this.courseService = courseService;
		this.registrationService = registrationService;
	}
	
	@GetMapping("/")
	public ModelAndView home(ModelAndView mv) {
		
		mv.setViewName("index");
		return mv;
	}
	
	@RequestMapping(value = "/courses", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageCourse(ModelAndView mv) {
		
		mv.addObject("courses", courseService.getAllCourses());
		mv.setViewName("course_manage");
		return mv;
	}
	
	@RequestMapping(value = "/students", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageStudent(ModelAndView mv) {
		
		mv.addObject("courses", courseService.getAllCourses());
		mv.addObject("students", registrationService.getAllEnrolledStudents());
		mv.setViewName("student_manage");
		return mv;
	}
	
	@GetMapping("favicon.ico")
	@ResponseBody
	public void doNothing() {
		//no favicon
	}
}
