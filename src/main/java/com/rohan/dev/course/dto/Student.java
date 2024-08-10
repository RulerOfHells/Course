package com.rohan.dev.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class Student {
	@Size(min = 3, message = "Student name must be 3 letters or higher")
	private String studentName;
	
	@Min(value = 14, message = "Student age must be 14 years or higher")
	private int age;
	
	@Min(value = 1, message = "Student ID nust be 1 or higher")
	private int rollNo;
	

	private Integer[] courses;
	
	public Student(int rollNo, String studentName, int age, Integer[] courses) {
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
	
	public Integer[] getCourses() {
		return courses;
	}
	
	public void setCourses(Integer[] courses) {
		this.courses = courses;
	}
	
}
