package com.rohan.dev.course.dao;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rohan.dev.course.dto.Course;

@Repository
public class CourseDAO {
	
	private JdbcTemplate jdbcTemplate;
	private BeanPropertyRowMapper<Course> rowMapper;
	private String sql;
	
	@Autowired
	public CourseDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = BeanPropertyRowMapper.newInstance(Course.class);
		init();
	}
	
	private void init() {
		jdbcTemplate.execute("create table if not exists Courses(ID int, CourseName varchar(20))");
	}
	
	public void addCourse(Course course) {
		sql = "insert into courses values(?, ?)";
		jdbcTemplate.update(sql, course.getCourseID(), course.getCourseName());
	}
	
	public void batchAdd(List<Course> courses) {
		sql = "insert into courses values(?, ?)";
		
		List<Object[]> args = new LinkedList<>();
		
		for(var course : courses)
			args.add(new Object[]{course.getCourseID(), course.getCourseName()});
		
		jdbcTemplate.batchUpdate(sql, args);
	}
	
	public List<Course> getAllCourses() {
		sql = "select ID as courseID, courseName from courses";
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	public void updateCourse(int id, Course course) {
		sql = "update courses set id=?, courseName=? where id=?";
		jdbcTemplate.update(sql, course.getCourseID(), course.getCourseName(), id);
	}
	
	public void deleteCourse(int id) {
		sql = "delete from courses where id=?";
		jdbcTemplate.update(sql, id);
	}
	
	public void clear() {
		sql = "truncate table courses";
		jdbcTemplate.execute(sql);
	}
}
