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

import com.rohan.dev.course.dto.Student;
import com.rohan.dev.course.exceptions.InvalidStudentException;
import com.rohan.dev.course.exceptions.StudentNotFoundException;
import com.rohan.dev.course.services.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class APIStudentController {

	@Autowired
	StudentService studentService;
	
	@PostMapping("/students")
	public Student createStudent(@Valid @RequestBody Student student) throws InvalidStudentException {
		try {
			studentService.addStudent(student);
		}
		catch(IllegalArgumentException e) {
			throw new InvalidStudentException("Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
		}
		return student;
	}
	
	@GetMapping("/students")
	public List<Student> getStudents() throws StudentNotFoundException {
		try {
			return studentService.getAllStudents();
		}
		catch(IllegalStateException e) {
			throw new StudentNotFoundException("No students available!");
		}
	}
	
	@GetMapping("/students/{id}")
	public Student getStudent(@PathVariable int id) throws StudentNotFoundException {
		try {
			return studentService.getStudentById(id);
		}
		catch(IllegalArgumentException e) {
			throw new StudentNotFoundException("Student with Roll No: " + id +" not found!");
		}
	}
	
	@PutMapping("/students/{id}")
	public Student updateStudent(@Valid @RequestBody Student student, @PathVariable int id) throws StudentNotFoundException, InvalidStudentException {
		try {
			studentService.updateStudent(id, student);
		}
		catch(IllegalArgumentException e) {
			throw new StudentNotFoundException("Student with Roll No: " + id + " doesn't exist!");
		}
		catch(IllegalStateException e) {
			throw new InvalidStudentException("Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
		}
		return student;
	}
	
	@DeleteMapping("/students/{id}")
	public Student deleteStudent(@PathVariable int id) throws StudentNotFoundException {
		try {
			return studentService.removeStudent(id);
		}
		catch (IllegalStateException e) {
			throw new StudentNotFoundException("Student with Roll No: " + id + " doesn't exists!");
		}
	}
	
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletResponse res) throws IOException {
    	res.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
	
	@ExceptionHandler(InvalidStudentException.class)
	public void handleInvalidCourse(InvalidStudentException ex, HttpServletResponse res) throws IOException {
		res.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
	}
	
	@ExceptionHandler(StudentNotFoundException.class)
	public void handleInvalidCourse(StudentNotFoundException ex, HttpServletResponse res) throws IOException {
		res.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
	}
}
