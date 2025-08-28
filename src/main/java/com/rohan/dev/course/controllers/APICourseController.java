package com.rohan.dev.course.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rohan.dev.course.dto.Course;
import com.rohan.dev.course.exceptions.CourseNotFoundException;
import com.rohan.dev.course.exceptions.InvalidCourseException;
import com.rohan.dev.course.services.CourseService;
import com.rohan.dev.course.services.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class APICourseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	StudentService studentService;
	
	@PostMapping("/courses")
	public Course createCourse(@Valid @RequestBody Course course) throws IOException, InvalidCourseException {
		try {
			courseService.addCourse(course);
		}
		catch(IllegalArgumentException e) {
			throw new InvalidCourseException("Invalid course! Course with ID: " + course.getCourseID() + " already exists!");
		}
		return course;
	}
	
	@GetMapping("/courses")
	public List<Course> getCourses() throws IOException, CourseNotFoundException {
		try {
			return courseService.getAllCourses();
		}
		catch(IllegalStateException e) {
			throw new CourseNotFoundException("No courses available!");
		}
	}
	
	@GetMapping("/courses/{id}")
	public Course getCourse(@PathVariable int id) throws IOException, CourseNotFoundException {
		try {
			return courseService.getCourse(id);
		}
		catch(IllegalArgumentException e) {
			throw new CourseNotFoundException("Course with id " + id +" not found!");
		}
	}
	
	@PutMapping("/courses/{id}")
	public Course updateCourse(@Valid @RequestBody Course course, @PathVariable int id) throws IOException, CourseNotFoundException, InvalidCourseException {
		
		try {
			if(id != course.getCourseID())
				studentService.unassignCourseFromAllStudents(id);
			courseService.updateCourse(id, course);
		}
		catch(IllegalArgumentException e) {
			throw new CourseNotFoundException("Course with ID: " + id + " doesn't exist!");
		}
		catch(IllegalStateException e) {
			throw new InvalidCourseException("Invalid course! Course with ID: " + course.getCourseID() + " already exists");
		}
		return course;
	}
	
	@DeleteMapping("/courses/{id}")
	public Course deleteCourse(@PathVariable int id) throws IOException, CourseNotFoundException {
		try {
			studentService.unassignCourseFromAllStudents(id);
			return courseService.removeCourse(id);
		}
		catch (IllegalStateException e) {
			throw new CourseNotFoundException("Course with ID: " + id + " doesn't exists!");
		}
	}
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletResponse res) throws IOException {
    	res.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
	
	@ExceptionHandler(InvalidCourseException.class)
	public void handleInvalidCourse(InvalidCourseException ex, HttpServletResponse res) throws IOException {
		res.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler(CourseNotFoundException.class)
	public void handleInvalidCourse(CourseNotFoundException ex, HttpServletResponse res) throws IOException {
		res.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
	}
}
