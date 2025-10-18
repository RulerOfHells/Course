package com.rohan.dev.course.service;

import java.util.List;

import com.rohan.dev.course.dto.CourseDTO;

public interface CourseService {
	public CourseDTO getCourse(int id);
	public List<CourseDTO> getAllCourses();
	public void addCourse(CourseDTO course);
	public void updateCourse(int id, CourseDTO newCourse);
	public CourseDTO removeCourse(int id);
}
