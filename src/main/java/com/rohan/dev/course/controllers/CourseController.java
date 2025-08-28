package com.rohan.dev.course.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rohan.dev.course.dto.Course;
import com.rohan.dev.course.exceptions.CourseNotFoundException;
import com.rohan.dev.course.exceptions.InvalidCourseException;
import com.rohan.dev.course.services.CourseService;
import com.rohan.dev.course.services.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	StudentService studentService;
	
	@PostMapping("/courses/add")
	public String createCourse(@Valid Course course, BindingResult result, RedirectAttributes redirectAttribute) throws IOException, InvalidCourseException {
		
		if(result.hasErrors()) {
			redirectAttribute.addFlashAttribute("org.springframework.validation.BindingResult.errCourse", result);
			redirectAttribute.addFlashAttribute("errCourse", course);
			return "redirect:/courses";
		}
		
		try {
			courseService.addCourse(course);
		}
		catch(IllegalArgumentException e) {
			throw new InvalidCourseException("Invalid course. Course with ID: " + course.getCourseID() + " already exists!");
		}
		
		return "redirect:/courses";
	}
	
	@GetMapping("/courses/get/{id}")
	public Course getCourse(@PathVariable int id) throws IOException, CourseNotFoundException {
		try {
			return courseService.getCourse(id);
		}
		catch(IllegalArgumentException e) {
			throw new CourseNotFoundException("Course with id " + id +" not found!");
		}
	}
	
	@PostMapping("/courses/update/{id}")
	public String updateCourse(@Valid Course course, BindingResult result, @PathVariable int id, RedirectAttributes redirectAttribute) throws IOException, InvalidCourseException, CourseNotFoundException {
		
		if(result.hasErrors()) {
			redirectAttribute.addFlashAttribute("org.springframework.validation.BindingResult.errCourse", result);
			redirectAttribute.addFlashAttribute("errCourse", course);
			return "redirect:/courses";
		}
		
		try {
			if(id != course.getCourseID())
				studentService.unassignCourseFromAllStudents(id);
			courseService.updateCourse(id, course);
		}
		catch(IllegalArgumentException e) {
			throw new CourseNotFoundException("Course with ID: " + id + " doesn't exist!");
		}
		catch(IllegalStateException e) {
			throw new InvalidCourseException("Invalid course. Course with ID: " + course.getCourseID() + " already exists!");
		}
		return "redirect:/courses";
	}
	
	@GetMapping("/courses/remove/{id}")
	public String deleteCourse(@PathVariable int id) throws IOException, CourseNotFoundException {
		try {
			studentService.unassignCourseFromAllStudents(id);
			courseService.removeCourse(id);
		}
		catch (IllegalStateException e) {
			throw new CourseNotFoundException("Course with ID: " + id + " doesn't exist!");
		}
		return "redirect:/courses";
	}
	
	@ExceptionHandler(InvalidCourseException.class)
	public String handleInvalidCourse(InvalidCourseException ex, HttpServletResponse res, RedirectAttributes redirectAttributes) {
		res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		redirectAttributes.addFlashAttribute("exceptionMsg", ex.getMessage());
		return "redirect:/courses";
	}
	
	@ExceptionHandler(CourseNotFoundException.class)
	public String handleInvalidCourse(CourseNotFoundException ex, HttpServletResponse res, RedirectAttributes redirectAttributes) {
		res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		redirectAttributes.addFlashAttribute("exceptionMsg", ex.getMessage());
		return "redirect:/courses";
	}
}
