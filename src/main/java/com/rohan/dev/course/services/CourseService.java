package com.rohan.dev.course.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.dao.CourseDAO;
import com.rohan.dev.course.dto.Course;

@Service
public class CourseService {
	
	private CourseDAO courseDAO;
	
	@Autowired
	public CourseService(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
	}
	
	private Course getCourseByID(int id) throws NoSuchElementException {
		Course c = courseDAO.getCourse(id);
		if(c == null)
			throw new NoSuchElementException();
		return c;
	}
	
	private boolean exists(int id) {
		try {
			getCourseByID(id);
		}
		catch(NoSuchElementException e) {
			return false;
		}
		return true;
	}
	
	public List<Course> getAllCourses() throws IllegalStateException {
		List<Course> courses = courseDAO.getAllCourses();
		if(courses.isEmpty())
			throw new IllegalStateException();
		return courses;
	}
	
	public Course getCourse(int id) throws IllegalArgumentException {
		if(!exists(id))
			throw new IllegalArgumentException();
		return getCourseByID(id);
	}
	
	public void addCourse(Course course) throws IllegalArgumentException {
		if(exists(course.getCourseID()))
			throw new IllegalArgumentException();
		courseDAO.addCourse(course);
	}
	
	public void updateCourse(int id, Course newCourse) throws IllegalArgumentException, IllegalStateException {
		try {
			if(exists(newCourse.getCourseID()) && id != newCourse.getCourseID())
				throw new IllegalStateException();
			Course course = getCourseByID(id);
			course.setCourseID(newCourse.getCourseID());
			course.setCourseName(newCourse.getCourseName());
			
			courseDAO.updateCourse(id, course);
		}
		catch (NoSuchElementException e) {
			throw new IllegalArgumentException();
		}
	}
	
	public Course removeCourse(int id) throws IllegalStateException {
		if (exists(id)) {
			Course course = getCourseByID(id);
			courseDAO.deleteCourse(id);
			return course;
		}
		throw new IllegalStateException();
	}
	
}
