package com.rohan.dev.course.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rohan.dev.course.dto.Course;
import com.rohan.dev.course.dto.Student;
import com.rohan.dev.course.editors.StudentCoursesEditor;
import com.rohan.dev.course.exceptions.InvalidStudentException;
import com.rohan.dev.course.exceptions.StudentNotFoundException;
import com.rohan.dev.course.services.StudentService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class StudentController {
	
	@Autowired
	StudentService studentService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Course.class,"courses", new StudentCoursesEditor());
	}
	
	
	@PostMapping("/students/add")
	public String createStudent(@Valid Student student, BindingResult result, RedirectAttributes redirectAttributes) throws IOException, InvalidStudentException {
		
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.errStudent", result);
			redirectAttributes.addFlashAttribute("errStudent", student);
			return "redirect:/students";
		}
		
		try {
			studentService.addStudent(student);
		}
		catch(IllegalArgumentException e) {
			throw new InvalidStudentException("Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
		}
		
		return "redirect:/students";
	}
	
	@PostMapping("/students/update/{id}")
	public String updateStudent(@Valid Student student, BindingResult result, @PathVariable int id, RedirectAttributes redirectAttributes) throws IOException, StudentNotFoundException, InvalidStudentException {
		
		if(result.hasErrors()) {
			redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.errStudent", result);
			redirectAttributes.addFlashAttribute("errStudent", student);
			return "redirect:/students";
		}
		
		try {
			studentService.updateStudent(id, student);
		}
		catch(IllegalArgumentException e) {
			throw new StudentNotFoundException("Student with Roll No: " + id + " doesn't exist!");
		}
		catch(IllegalStateException e) {
			throw new InvalidStudentException("Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
		}
		return "redirect:/students";
	}
	
	@GetMapping("/students/remove/{id}")
	public String deleteStudent(@PathVariable int id) throws IOException, StudentNotFoundException {
		try {
			studentService.removeStudent(id);
		}
		catch (IllegalStateException e) {
			throw new StudentNotFoundException("Student with Roll No: " + id + " doesn't exist!");
		}
		return "redirect:/students";
	}
	
	@ExceptionHandler(InvalidStudentException.class)
	public String handleInvalidStudent(InvalidStudentException ex, HttpServletResponse res, RedirectAttributes redirectAttributes) {
		res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		redirectAttributes.addFlashAttribute("exceptionMsg", ex.getMessage());
		return "redirect:/students";
	}
	
	@ExceptionHandler(StudentNotFoundException.class)
	public String handleInvalidCourse(StudentNotFoundException ex, HttpServletResponse res, RedirectAttributes redirectAttributes) {
		res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		redirectAttributes.addFlashAttribute("exceptionMsg", ex.getMessage());
		return "redirect:/students";
	}
}
