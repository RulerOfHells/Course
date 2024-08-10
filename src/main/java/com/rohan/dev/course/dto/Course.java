package com.rohan.dev.course.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class Course {
	@Min(value = 1, message = "Course ID should be atleast 1")
	private int courseID;
	
	@Size(min = 4, message = "Course name must be 4 letters or higher")
	private String courseName;
	
	public Course(int courseID, String courseName) {
		this.setCourseID(courseID);
		this.setCourseName(courseName);
	}

	public Course() {}
	
	public int getCourseID() {
		return courseID;
	}

	public void setCourseID(int courseID) {
		this.courseID = courseID;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	@Override
	public String toString() {
		return courseID + ": " + courseName;
	}
}
