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

import com.rohan.dev.course.dto.Student;
import com.rohan.dev.course.services.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class APIStudentController {

	@Autowired
	StudentService studentService;
	
	@PostMapping("/students")
	public Student createStudent(@Valid @RequestBody Student student, BindingResult result, HttpServletResponse res) throws IOException {
		
		if(result.hasErrors()) {
			res.sendError(400, result.getAllErrors().get(0).getDefaultMessage());
			return null;
		}
		
		try {
			studentService.addStudent(student);
		}
		catch(IllegalArgumentException e) {
			res.sendError(400, "Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
		}
		return student;
	}
	
	@GetMapping("/students")
	public List<Student> getStudents(HttpServletResponse res) throws IOException {
		try {
			return studentService.getAllStudents();
		}
		catch(IllegalStateException e) {
			res.sendError(400, "No students available!");
		}
		return null;
	}
	
	@GetMapping("/students/{id}")
	public Student getStudent(@PathVariable int id, HttpServletResponse res) throws IOException {
		try {
			return studentService.getStudent(id);
		}
		catch(IllegalArgumentException e) {
			res.sendError(404, "Student with Roll No: " + id +" not found!");
		}
		return null;
	}
	
	@PutMapping("/students/{id}")
	public Student updateStudent(@Valid @RequestBody Student student, BindingResult result, @PathVariable int id, HttpServletResponse res) throws IOException {
		
		if(result.hasErrors()) {
			res.sendError(400, result.getAllErrors().get(0).getDefaultMessage());
			return null;
		}
		
		try {
			studentService.updateStudent(id, student);
		}
		catch(IllegalArgumentException e) {
			res.sendError(404, "Student with Roll No: " + id + " doesn't exist!");
		}
		catch(IllegalStateException e) {
			res.sendError(400, "Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
			return null;
		}
		return student;
	}
	
	@DeleteMapping("/students/{id}")
	public Student deleteStudent(@PathVariable int id, HttpServletResponse res) throws IOException {
		try {
			return studentService.removeStudent(id);
		}
		catch (IllegalStateException e) {
			res.sendError(404, "Student with Roll No: " + id + " doesn't exists!");
		}
		return null;
	}
}
