package com.rohan.dev.course.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.dev.course.dto.Course;
import com.rohan.dev.course.services.CourseService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class APICourseController {
	
	@Autowired
	CourseService courseService;
	
	@PostMapping("/courses")
	public Course createCourse(@Valid @RequestBody Course course, BindingResult result, HttpServletResponse res) throws IOException {
		
		if(result.hasErrors()) {
			res.sendError(400, result.getAllErrors().get(0).getDefaultMessage());
			return null;
		}
		
		try {
			courseService.addCourse(course);
		}
		catch(IllegalArgumentException e) {
			res.sendError(400, "Invalid course");
		}
		return course;
	}
	
	@GetMapping("/courses")
	public List<Course> getCourses(HttpServletResponse res) throws IOException {
		try {
			return courseService.getAllCourses();
		}
		catch(IllegalStateException e) {
			res.sendError(400, "No courses available!");
		}
		return null;
	}
	
	@GetMapping("/courses/{id}")
	public Course getCourse(@PathVariable int id, HttpServletResponse res) throws IOException {
		try {
			return courseService.getCourse(id);
		}
		catch(IllegalArgumentException e) {
			res.sendError(404, "Course with id " + id +" not found!");
		}
		return null;
	}
	
	@PutMapping("/courses/{id}")
	public Course updateCourse(@Valid @RequestBody Course course, BindingResult result, @PathVariable int id, HttpServletResponse res) throws IOException {
		
		if(result.hasErrors()) {
			res.sendError(400, result.getAllErrors().get(0).getDefaultMessage());
			return null;
		}
		
		try {
			courseService.updateCourse(id, course);
		}
		catch(IllegalArgumentException e) {
			res.sendError(404, "Course with ID: " + id + " doesn't exist!");
		}
		return course;
	}
	
	@DeleteMapping("/courses/{id}")
	public Course deleteCourse(@PathVariable int id, HttpServletResponse res) throws IOException {
		try {
			return courseService.removeCourse(id);
		}
		catch (IllegalStateException e) {
			res.sendError(404, "Course with ID: " + id + " doesn't exists!");
		}
		return null;
	}
}
