package com.rohan.dev.course.repository.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rohan.dev.course.domain.Course;
import com.rohan.dev.course.repository.CourseRepository;

@Repository
public class CourseRepositoryImpl implements CourseRepository {
	
	private JdbcTemplate jdbcTemplate;
	private BeanPropertyRowMapper<Course> rowMapper;
	private String sql;
	
	@Autowired
	public CourseRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = BeanPropertyRowMapper.newInstance(Course.class);
	}
	
	@Override
	public void addCourse(Course course) {
		sql = "insert into Courses values(?, ?)";
		jdbcTemplate.update(sql, course.getCourseID(), course.getCourseName());
	}
	
	@Override
	public void batchAdd(List<Course> courses) {
		sql = "insert into Courses values(?, ?)";
		
		List<Object[]> args = new LinkedList<>();
		
		for(var course : courses)
			args.add(new Object[]{course.getCourseID(), course.getCourseName()});
		
		jdbcTemplate.batchUpdate(sql, args);
	}
	
	@Override
	public List<Course> getAllCourses() {
		sql = "select courseID, courseName from Courses";
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public Course getCourseById(int id) {
		sql = "select courseID, courseName from Courses where courseID = ?";
		List<Course> course = jdbcTemplate.query(sql, rowMapper, id);
		if(course.size() == 0)
			return null;
		return course.get(0);
		
	}
	
	@Override
	public void updateCourse(int id, Course course) {
		sql = "update Courses set courseID=?, courseName=? where courseID=?";
		jdbcTemplate.update(sql, course.getCourseID(), course.getCourseName(), id);
	}
	
	@Override
	public void deleteCourse(int id) {
		sql = "delete from Courses where courseID=?";
		jdbcTemplate.update(sql, id);
	}
}
