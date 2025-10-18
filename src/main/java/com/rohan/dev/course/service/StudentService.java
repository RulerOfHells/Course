package com.rohan.dev.course.service;

import java.util.List;

import com.rohan.dev.course.dto.StudentDTO;

public interface StudentService {
	public List<Integer> getStudentCourses(int roll);
	public List<StudentDTO> getAllStudents();
	public StudentDTO getStudent(int rollNo);
	public void addStudent(StudentDTO student);
	public void updateStudent(int id, StudentDTO newStudent);
	public StudentDTO removeStudent(int id);
	
	public void unassignCourseFromAllStudents(int courseId);
}
