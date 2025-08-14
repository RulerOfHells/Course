package com.rohan.dev.course.services;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.dao.StudentDAO;
import com.rohan.dev.course.dto.Course;
import com.rohan.dev.course.dto.Student;

@Service
public class StudentService {
	
	private StudentDAO studentDAO;
	
	private CourseService courseService;
	
	@Autowired
	public StudentService(StudentDAO studentDAO, CourseService courseService) {
		this.studentDAO = studentDAO;
		this.courseService = courseService;
	}
	
	private Student getStudentById(int id) throws NoSuchElementException {
		Student s = studentDAO.getStudent(id);
		if(s == null)
			throw new NoSuchElementException();
		List<Course> newCourses = new LinkedList<>();
		Integer[] courseIDs = studentDAO.getStudentCourses(s.getRollNo()).toArray(new Integer[0]);
		for(var cid : courseIDs)
			newCourses.add(courseService.getCourse(cid));			
		s.setCourses(newCourses);
		return s;
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
		List<Student> s = studentDAO.getAllStudents();
		if(s == null)
			throw new IllegalStateException();
		List<Student> students = new LinkedList<>();
		for(Student student : s)
			students.add(getStudentById(student.getRollNo()));
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
		studentDAO.deleteStudent(id);
		return student;
	}
}
