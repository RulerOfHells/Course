package com.rohan.dev.course.repository;

import java.util.List;

import com.rohan.dev.course.domain.Student;

public interface StudentRepository {
	public void addStudent(Student student);
	public void batchAdd(List<Student> students);
	public List<Student> getAllStudents();
	public Student getStudent(int rollNo);
	public void updateStudent(int id, Student student);
	public void deleteStudent(int id);
	
	public void unassignCourseFromAllStudents(int courseId);
	public List<Integer> getStudentCourses(int rollNo);
}
