package com.rohan.dev.course.domain;

import java.util.List;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class Student {
	@Size(min = 3, message = "{student.valid.name}")
	private String studentName;
	
	@Min(value = 14, message = "{student.valid.age}")
	private int age;
	
	@Min(value = 1, message = "{student.valid.roll}")
	private int rollNo;
	

	private List<Integer> courses;
	
	public Student(int rollNo, String studentName, int age, List<Integer> courses) {
		this.studentName = studentName;
		this.age = age;
		this.courses = courses;
		this.rollNo = rollNo;
	}
	
	public Student() {}
	
	public int getRollNo() {
		return rollNo;
	}
	
	public void setRollNo(int studentID) {
		this.rollNo = studentID;
	}
	
	public String getStudentName() {
		return studentName;
	}
	
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public List<Integer> getCourses() {
		return courses;
	}
	
	public void setCourses(List<Integer> courses) {
		this.courses = courses;
	}

}
