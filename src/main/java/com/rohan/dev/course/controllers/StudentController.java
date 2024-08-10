package com.rohan.dev.course.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.rohan.dev.course.dto.Student;
import com.rohan.dev.course.services.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@PostMapping("/students/add")
	public String createStudent(@Valid Student student, BindingResult result, HttpServletResponse res) throws IOException {
		
		if(result.hasErrors()) {
			res.sendError(400, result.getAllErrors().get(0).getDefaultMessage());
			return null;
		}
		
		try {
			studentService.addStudent(student);
		}
		catch(IllegalArgumentException e) {
			res.sendError(400, "Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
			return null;
		}
		
		return "redirect:/students";
	}
	
	@GetMapping("/students/get/{id}")
	public Student getStudent(@PathVariable int id, HttpServletResponse res) throws IOException {
		try {
			return studentService.getStudent(id);
		}
		catch(IllegalArgumentException e) {
			res.sendError(404, "Student with roll no " + id +" not found!");
		}
		return null;
	}
	
	@PostMapping("/students/update/{id}")
	public String updateStudent(@Valid Student student, BindingResult result, @PathVariable int id, HttpServletResponse res) throws IOException {
		
		if(result.hasErrors()) {
			res.sendError(400, result.getAllErrors().get(0).getDefaultMessage());
			return null;
		}
		
		try {
			studentService.updateStudent(id, student);
		}
		catch(IllegalArgumentException e) {
			res.sendError(404, "Student with Roll No: " + id + " doesn't exist!");
			return null;
		}
		catch(IllegalStateException e) {
			res.sendError(400, "Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
			return null;
		}
		return "redirect:/students";
	}
	
	@GetMapping("/students/remove/{id}")
	public String deleteStudent(@PathVariable int id, HttpServletResponse res) throws IOException {
		try {
			studentService.removeStudent(id);
		}
		catch (IllegalStateException e) {
			res.sendError(404, "Student with Roll No: " + id + " doesn't exist!");
			return null;
		}
		return "redirect:/students";
	}
}
