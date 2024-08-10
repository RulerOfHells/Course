package com.rohan.dev.course.services;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.dao.CourseDAO;
import com.rohan.dev.course.dto.Course;

@Service
public class CourseService {
	
	private CourseDAO courseDAO;
	private List<Course> courses;
	
	@Autowired
	public CourseService(CourseDAO courseDAO) {
		this.courseDAO = courseDAO;
		init();
	}
	
	private void init() {
		courses = courseDAO.getAllCourses();
		
		if(courses == null)
			courses = new LinkedList<>();
	}
	
	private Course getCourseByID(int id) throws NoSuchElementException {
		return courses.stream().filter(c -> c.getCourseID() == id).findFirst().get();
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
		courses.add(course);
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
			courses.remove(course);
			courseDAO.deleteCourse(id);
			return course;
		}
		throw new IllegalStateException();
	}
	
}
