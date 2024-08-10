package com.rohan.dev.course.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rohan.dev.course.services.CourseService;
import com.rohan.dev.course.services.StudentService;

@Controller
public class HomePageController {
	
	private Map<Integer, String> courseMap;
	
	private CourseService courseService;
	
	private StudentService studentService;
	
	@Autowired
	public HomePageController(CourseService courseService, StudentService studentService, Map<Integer, String> courseMap) {
		this.courseService = courseService;
		this.studentService = studentService;
		this.courseMap = courseMap;
		
		for(var course : courseService.getAllCourses()) {
			courseMap.put(course.getCourseID(), course.getCourseName());
		}
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
		
		mv.addObject("courseMap", courseMap);
		mv.addObject("students", studentService.getAllStudents());
		mv.setViewName("student_manage");
		return mv;
	}
	
	@GetMapping("favicon.ico")
	@ResponseBody
	public void doNothing() {
		//no favicon
	}
}
