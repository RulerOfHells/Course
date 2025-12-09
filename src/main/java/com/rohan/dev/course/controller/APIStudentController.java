package com.rohan.dev.course.controller;

import com.rohan.dev.course.domain.APIResponse;
import com.rohan.dev.course.dto.StudentDTO;
import com.rohan.dev.course.exceptions.InvalidStudentException;
import com.rohan.dev.course.exceptions.StudentNotFoundException;
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
public class APIStudentController {

	@Autowired
	StudentService studentService;
	
	@PostMapping("/students")
	public ResponseEntity<APIResponse> createStudent(@Valid @RequestBody StudentDTO student) throws InvalidStudentException {
		try {
			studentService.addStudent(student);
		}
		catch(IllegalArgumentException e) {
			throw new InvalidStudentException("Invalid student. Student with Roll No: " + student.getRollNo() + " already exists!");
		}
		return ResponseEntity.created(getUri()).body(
				new APIResponse().setTimeStamp(now().toString())
				 .setData(Map.of("student", student))
				 .setMessage("Student created!")
				 .setStatus(HttpStatus.CREATED)
				 .setStatusCode(HttpStatus.CREATED.value())
				);
	}
	
	@GetMapping("/students")
	public List<StudentDTO> getStudents() throws StudentNotFoundException {
		try {
			return studentService.getAllStudents();
		}
		catch(IllegalStateException e) {
			throw new StudentNotFoundException("No students available!");
		}
	}
	
	@GetMapping("/students/{id}")
	public StudentDTO getStudent(@PathVariable int id) throws StudentNotFoundException {
		try {
			return studentService.getStudent(id);
		}
		catch(IllegalArgumentException e) {
			throw new StudentNotFoundException("Student with Roll No: " + id +" not found!");
		}
	}
	
	@PutMapping("/students/{id}")
	public StudentDTO updateStudent(@Valid @RequestBody StudentDTO student, @PathVariable int id) throws StudentNotFoundException, InvalidStudentException {
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
	
	@DeleteMapping("/students/remove/{id}")
	public StudentDTO deleteStudent(@PathVariable int id) throws StudentNotFoundException {
		try {
			return studentService.removeStudent(id);
		}
		catch (IllegalStateException e) {
			throw new StudentNotFoundException("Student with Roll No: " + id + " doesn't exists!");
		}
	}
	
	private URI getUri() {
		return URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/students/<studentID>").toUriString());
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
	
	@ExceptionHandler(InvalidStudentException.class)
	public ResponseEntity<APIResponse> handleInvalidStudent(InvalidStudentException ex) {
        return ResponseEntity.badRequest().body(
                new APIResponse()
                        .setTimeStamp(now().toString())
                        .setStatus(HttpStatus.BAD_REQUEST)
                        .setStatusCode(HttpStatus.BAD_REQUEST.value())
                        .setMessage("Invalid Student ID!")
                        .setError(ex.getMessage())
        );
	}
	
	@ExceptionHandler(StudentNotFoundException.class)
	public ResponseEntity<APIResponse> handleStudentNotFound(StudentNotFoundException ex) {
        return ResponseEntity.badRequest().body(
                new APIResponse()
                        .setTimeStamp(now().toString())
                        .setStatus(HttpStatus.NOT_FOUND)
                        .setStatusCode(HttpStatus.NOT_FOUND.value())
                        .setMessage("Student not found!")
                        .setError(ex.getMessage())
        );
	}
}
