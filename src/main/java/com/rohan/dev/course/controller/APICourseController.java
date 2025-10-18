package com.rohan.dev.course.controller;

import static java.time.LocalDateTime.now;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rohan.dev.course.domain.APIResponse;
import com.rohan.dev.course.dto.CourseDTO;
import com.rohan.dev.course.exceptions.CourseNotFoundException;
import com.rohan.dev.course.exceptions.InvalidCourseException;
import com.rohan.dev.course.service.CourseService;
import com.rohan.dev.course.service.StudentService;

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
	public ResponseEntity<APIResponse> createCourse(@Valid @RequestBody CourseDTO course) throws IOException, InvalidCourseException {
		try {
			courseService.addCourse(course);
		}
		catch(IllegalArgumentException e) {
			throw new InvalidCourseException("Invalid course! Course with ID: " + course.getCourseID() + " already exists!");
		}
		return ResponseEntity.created(getUri()).body(
				new APIResponse().setTimeStamp(now().toString())
				 .setData(Map.of("course", course))
				 .setMessage("Course created!")
				 .setStatus(HttpStatus.CREATED)
				 .setStatusCode(HttpStatus.CREATED.value())
				);
	}
	
	@GetMapping("/courses")
	public List<CourseDTO> getCourses() throws IOException, CourseNotFoundException {
		try {
			return courseService.getAllCourses();
		}
		catch(IllegalStateException e) {
			throw new CourseNotFoundException("No courses available!");
		}
	}
	
	@GetMapping("/courses/{id}")
	public CourseDTO getCourse(@PathVariable int id) throws IOException, CourseNotFoundException {
		try {
			return courseService.getCourse(id);
		}
		catch(IllegalArgumentException e) {
			throw new CourseNotFoundException("Course with id " + id +" not found!");
		}
	}
	
	@PutMapping("/courses/{id}")
	public CourseDTO updateCourse(@Valid @RequestBody CourseDTO course, @PathVariable int id) throws IOException, CourseNotFoundException, InvalidCourseException {
		
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
	public CourseDTO deleteCourse(@PathVariable int id) throws IOException, CourseNotFoundException {
		try {
			studentService.unassignCourseFromAllStudents(id);
			return courseService.removeCourse(id);
		}
		catch (IllegalStateException e) {
			throw new CourseNotFoundException("Course with ID: " + id + " doesn't exists!");
		}
	}
	
	private URI getUri() {
		return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/courses/<courseID>").toUriString());
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
