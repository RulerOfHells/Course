package com.rohan.dev.course.repository;

import java.util.List;

import com.rohan.dev.course.domain.Course;

public interface CourseRepository {
	public void addCourse(Course course);
	public void batchAdd(List<Course> courses);
	public List<Course> getAllCourses();
	public Course getCourseById(int id);
	public void updateCourse(int id, Course course);
	public void deleteCourse(int id);
}
