package com.rohan.dev.course.services;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.dao.StudentDAO;
import com.rohan.dev.course.dto.Student;

@Service
public class StudentService {
	
	private StudentDAO studentDAO;
	private List<Student> students;
	
	@Autowired
	public StudentService(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
		init();
	}
	
	private void init() {
		students = studentDAO.getAllStudents();
		
		if(students == null)
			students = new LinkedList<>();
		else
			for(var student : students)
				student.setCourses(studentDAO.getStudentCourses(student.getRollNo()).toArray(new Integer[0]));
	}
	
	private Student getStudentById(int id) throws NoSuchElementException {
		return students.stream().filter(s -> s.getRollNo() == id).findFirst().get();
	}
	
	private boolean exists(int id) {
		try {
			getStudentById(id);
		}
		catch(NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	public List<Student> getAllStudents() throws IllegalStateException {
		if(students.isEmpty())
			throw new IllegalStateException();
		return students;
	}	
	
	public Student getStudent(int id) throws IllegalArgumentException {
		if(!exists(id))
			throw new IllegalArgumentException();
		return getStudentById(id);
	}
	
	public void addStudent(Student student) throws IllegalArgumentException {
		if(exists(student.getRollNo()))
			throw new IllegalArgumentException();
		students.add(student);
		studentDAO.addStudent(student);
	}
	
	public void updateStudent(int id, Student newStudent) throws IllegalArgumentException, IllegalStateException {
		try {
			if(exists(newStudent.getRollNo()) && id != newStudent.getRollNo())
				throw new IllegalStateException();
			Student student = getStudentById(id);
			student.setAge(newStudent.getAge());
			student.setStudentName(newStudent.getStudentName());
			student.setRollNo(newStudent.getRollNo());
			student.setCourses(newStudent.getCourses());
			
			studentDAO.updateStudent(id, student);
		}
		catch(NoSuchElementException e) {
			throw new IllegalArgumentException();
		}
	}
	
	public Student removeStudent(int id) throws IllegalArgumentException {
		if(!exists(id))
			throw new IllegalArgumentException();
		Student student = getStudentById(id);
		students.remove(student);
		studentDAO.deleteStudent(id);
		return student;
	}
}
