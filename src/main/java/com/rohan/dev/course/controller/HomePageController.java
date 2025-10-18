package com.rohan.dev.course.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rohan.dev.course.exceptions.CourseNotFoundException;
import com.rohan.dev.course.exceptions.StudentNotFoundException;
import com.rohan.dev.course.service.CourseService;
import com.rohan.dev.course.service.StudentService;

@Controller
public class HomePageController {
	
	private CourseService courseService;
	
	private StudentService studentService;
	
	@Autowired
	public HomePageController(CourseService courseService, StudentService studentService) {
		this.courseService = courseService;
		this.studentService = studentService;
	}
	
	@GetMapping("/")
	public String goHome() {
		return "redirect:/home";
	}
	
	@GetMapping("/home")
	public ModelAndView home(ModelAndView mv) {
		
		mv.setViewName("index");
		return mv;
	}
	
	@RequestMapping(value = "/courses", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageCourse(ModelAndView mv) {
		try {
			mv.addObject("courses", courseService.getAllCourses());
			mv.setViewName("course_manage");
		} catch(IllegalStateException e) {
			throw new CourseNotFoundException("No courses available!");
		}
		return mv;
	}
	
	@RequestMapping(value = "/students", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView manageStudent(ModelAndView mv) {
		try {
			mv.addObject("courses", courseService.getAllCourses());
			mv.addObject("students", studentService.getAllStudents());
			mv.setViewName("student_manage");
		} catch(IllegalStateException e) {
			throw new StudentNotFoundException("No students available!");
		}
		return mv;
	}
	
	@GetMapping("favicon.ico")
	@ResponseBody
	public void doNothing() {
		//no favicon
	}
}
