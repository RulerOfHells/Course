package com.rohan.dev.course.dtomapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.rohan.dev.course.domain.Course;
import com.rohan.dev.course.dto.CourseDTO;

@Component
public class CourseDTOMapper {
	public static CourseDTO fromCourse(Course course) {
		if(course == null) return null;
		
		CourseDTO courseDTO = new CourseDTO();
		BeanUtils.copyProperties(course, courseDTO);
		return courseDTO;
	}
	
	public static Course toCourse(CourseDTO courseDTO) {
		if(courseDTO == null) return null;
		
		Course course = new Course();
		BeanUtils.copyProperties(courseDTO, course);
		return course;
	}
	
	public static List<CourseDTO> fromCourseList(List<Course> courses) {
		if(courses == null) return null;
		
		return courses.stream().map(CourseDTOMapper::fromCourse).collect(Collectors.toList());
	}
	
	public static List<Course> toCourseList(List<CourseDTO> courses) {
		if(courses == null) return null;
		
		return courses.stream().map(CourseDTOMapper::toCourse).toList();
	}
}
