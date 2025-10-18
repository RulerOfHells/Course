package com.rohan.dev.course.service.impl;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.dto.CourseDTO;
import com.rohan.dev.course.dtomapper.CourseDTOMapper;
import com.rohan.dev.course.repository.CourseRepository;
import com.rohan.dev.course.service.CourseService;

@Service
public class CourseServiceImpl implements CourseService {
	
	private CourseRepository courseDAO;
	
	@Autowired
	public CourseServiceImpl(CourseRepository courseDAO) {
		this.courseDAO = courseDAO;
	}
	
	private CourseDTO getCourseByID(int id) throws NoSuchElementException {
		CourseDTO c = CourseDTOMapper.fromCourse(courseDAO.getCourseById(id));
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
	
	@Override
	public List<CourseDTO> getAllCourses() throws IllegalStateException {
		List<CourseDTO> courses = CourseDTOMapper.fromCourseList(courseDAO.getAllCourses());
		if(courses.isEmpty())
			throw new IllegalStateException();
		return courses;
	}
	
	@Override
	public CourseDTO getCourse(int id) throws IllegalArgumentException {
		if(!exists(id))
			throw new IllegalArgumentException();
		return getCourseByID(id);
	}
	
	@Override
	public void addCourse(CourseDTO course) throws IllegalArgumentException {
		if(exists(course.getCourseID()))
			throw new IllegalArgumentException();
		courseDAO.addCourse(CourseDTOMapper.toCourse(course));
	}
	
	@Override
	public void updateCourse(int id, CourseDTO newCourse) throws IllegalArgumentException, IllegalStateException {
		try {
			if(exists(newCourse.getCourseID()) && id != newCourse.getCourseID())
				throw new IllegalStateException();
			CourseDTO course = getCourseByID(id);
			course.setCourseID(newCourse.getCourseID());
			course.setCourseName(newCourse.getCourseName());
			
			courseDAO.updateCourse(id, CourseDTOMapper.toCourse(course));
		}
		catch (NoSuchElementException e) {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public CourseDTO removeCourse(int id) throws IllegalStateException {
		if (exists(id)) {
			CourseDTO course = getCourseByID(id);
			courseDAO.deleteCourse(id);
			return course;
		}
		throw new IllegalStateException();
	}
}
