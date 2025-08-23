package com.rohan.dev.course.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.dao.StudentDAO;
import com.rohan.dev.course.dto.Student;

@Service
public class StudentService {
	
	private StudentDAO studentDAO;
	
	@Autowired
	public StudentService(StudentDAO studentDAO) {
		this.studentDAO = studentDAO;
	}
	
	public Student getStudentById(int id) throws IllegalArgumentException {
		Student s = studentDAO.getStudent(id);
		if(s == null)
			throw new IllegalArgumentException();
		s.setCourses(getStudentCourses(id));
		return s;
	}
	
	private boolean exists(int id) {
		try {
			getStudentById(id);
		}
		catch(IllegalArgumentException e) {
			return false;
		}
		return true;
	}
	
	public List<Student> getAllStudents() throws IllegalStateException {
		List<Student> students = studentDAO.getAllStudents();
		if(students == null)
			throw new IllegalStateException();
		for(Student student : students)
			student.setCourses(getStudentCourses(student.getRollNo()));
		return students;
	}
	
	
	public List<Integer> getStudentCourses(int roll) {
		return studentDAO.getStudentCourses(roll);
	}
	
	public void addStudent(Student student) throws IllegalArgumentException {
		if(exists(student.getRollNo()))
			throw new IllegalArgumentException();
		studentDAO.addStudent(student);
	}
	
	public void updateStudent(int id, Student newStudent) throws IllegalArgumentException, IllegalStateException {
		if(exists(newStudent.getRollNo()) && id != newStudent.getRollNo())
			throw new IllegalStateException();
		Student student = getStudentById(id);
		student.setAge(newStudent.getAge());
		student.setStudentName(newStudent.getStudentName());
		student.setRollNo(newStudent.getRollNo());
		student.setCourses(newStudent.getCourses());
			
		studentDAO.updateStudent(id, student);
	}
	
	public Student removeStudent(int id) throws IllegalArgumentException {
		if(!exists(id))
			throw new IllegalArgumentException();
		Student student = getStudentById(id);
		studentDAO.deleteStudent(id);
		return student;
	}
	
	public void unassignCourseFromAllStudents(int courseId) {
        studentDAO.unassignCourseFromAllStudents(courseId);
    }
}
