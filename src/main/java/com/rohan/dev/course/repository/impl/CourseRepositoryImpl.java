package com.rohan.dev.course.repository.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rohan.dev.course.domain.Course;
import com.rohan.dev.course.repository.CourseRepository;

import static com.rohan.dev.course.repository.query.CourseQuery.*;

@Repository
@Transactional
public class CourseRepositoryImpl implements CourseRepository {
	
	private final JdbcTemplate jdbcTemplate;
	private final BeanPropertyRowMapper<Course> rowMapper;
	
	@Autowired
	public CourseRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = BeanPropertyRowMapper.newInstance(Course.class);
	}
	
	@Override
	public void addCourse(Course course) {
		jdbcTemplate.update(INSERT_COURSE_QUERY, course.getCourseID(), course.getCourseName());
	}
	
	@Override
	public void batchAdd(List<Course> courses) {
		List<Object[]> args = new LinkedList<>();
		
		for(var course : courses)
			args.add(new Object[]{course.getCourseID(), course.getCourseName()});
		
		jdbcTemplate.batchUpdate(INSERT_COURSE_QUERY, args);
	}
	
	@Override
	public List<Course> getAllCourses() {
		return jdbcTemplate.query(SELECT_ALL_COURSE_QUERY, rowMapper);
	}
	
	@Override
	public Course getCourseById(int id) {
		List<Course> course = jdbcTemplate.query(SELECT_SINGLE_COURSE_QUERY, rowMapper, id);
		if(course.isEmpty())
			return null;
		return course.get(0);
		
	}
	
	@Override
	public void updateCourse(int id, Course course) {
		jdbcTemplate.update(UPDATE_COURSE_QUERY, course.getCourseID(), course.getCourseName(), id);
	}
	
	@Override
	public void deleteCourse(int id) {
		jdbcTemplate.update(DELETE_COURSE_QUERY, id);
	}
}
