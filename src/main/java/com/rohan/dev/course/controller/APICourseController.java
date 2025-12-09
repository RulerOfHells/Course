package com.rohan.dev.course.controller;

import com.rohan.dev.course.domain.APIResponse;
import com.rohan.dev.course.dto.CourseDTO;
import com.rohan.dev.course.exceptions.CourseNotFoundException;
import com.rohan.dev.course.exceptions.InvalidCourseException;
import com.rohan.dev.course.service.CourseService;
import com.rohan.dev.course.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("/api")
public class APICourseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	StudentService studentService;
	
	@PostMapping("/courses")
	public ResponseEntity<APIResponse> createCourse(@Valid @RequestBody CourseDTO course) throws InvalidCourseException {
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
	public List<CourseDTO> getCourses() throws CourseNotFoundException {
		try {
			return courseService.getAllCourses();
		}
		catch(IllegalStateException e) {
			throw new CourseNotFoundException("No courses available!");
		}
	}
	
	@GetMapping("/courses/{id}")
	public CourseDTO getCourse(@PathVariable int id) throws CourseNotFoundException {
		try {
			return courseService.getCourse(id);
		}
		catch(IllegalArgumentException e) {
			throw new CourseNotFoundException("Course with id " + id +" not found!");
		}
	}
	
	@PutMapping("/courses/{id}")
	public CourseDTO updateCourse(@Valid @RequestBody CourseDTO course, @PathVariable int id) throws CourseNotFoundException, InvalidCourseException {
		
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
	
	@DeleteMapping("/courses/remove/{id}")
	public CourseDTO deleteCourse(@PathVariable int id) throws CourseNotFoundException {
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
    public ResponseEntity<APIResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(
                new APIResponse()
                        .setTimeStamp(now().toString())
                        .setStatus(HttpStatus.BAD_REQUEST)
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setDevMsg("Binding error! Check your request body.")
                        .setError(ex.getBindingResult().getAllErrors().get(0).getDefaultMessage())
        );
    }
	
	@ExceptionHandler(InvalidCourseException.class)
	public ResponseEntity<APIResponse> handleInvalidCourse(InvalidCourseException ex) {
        return ResponseEntity.badRequest().body(
                new APIResponse()
                        .setMessage("Invalid Course ID!")
                        .setTimeStamp(now().toString())
                        .setStatus(HttpStatus.BAD_REQUEST)
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setError(ex.getMessage())
        );
	}
	
	@ExceptionHandler(CourseNotFoundException.class)
	public ResponseEntity<APIResponse> handleCourseNotFound(CourseNotFoundException ex) {
        return ResponseEntity.badRequest().body(
                new APIResponse()
                        .setMessage("Course not found!")
                        .setTimeStamp(now().toString())
                        .setStatus(HttpStatus.NOT_FOUND)
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setError(ex.getMessage())
        );
	}
}
