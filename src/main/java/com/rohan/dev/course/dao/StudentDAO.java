package com.rohan.dev.course.dao;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.rohan.dev.course.dto.Course;
import com.rohan.dev.course.dto.Student;

@Repository
public class StudentDAO {

	private JdbcTemplate jdbcTemplate;
	private BeanPropertyRowMapper<Student> rowMapper;
	private String sql;
	
	@Autowired
	public StudentDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = BeanPropertyRowMapper.newInstance(Student.class);
		init();
	}
	
	private void init() {
		jdbcTemplate.execute("create table if not exists Students(Roll_No int, StudentName varchar(20), Age int)");
		jdbcTemplate.execute("create table if not exists Students_Courses(Roll_No int references Students(Roll_No), ID int references Courses(ID), primary key(Roll_No, ID))");
	}
	
	public void addStudent(Student student) {
		sql = "insert into students values(?, ?, ?)";
		
		jdbcTemplate.update(sql, student.getRollNo(), student.getStudentName(), student.getAge());
		
		if(student.getCourses() == null) return;
		
		sql = "insert into students_courses values(?, ?)";
		
		for(var cid : student.getCourses())
			jdbcTemplate.update(sql, student.getRollNo(), cid.getCourseID());
	}
	
	public void batchAdd(List<Student> students) {
		sql = "insert into students values(?, ?, ?)";
		String sql2 = "insert into students_courses values(?, ?)";
		
		List<Object[]> args = new LinkedList<>();
		
		for(var student : students) {
			args.add(new Object[] {student.getRollNo(), student.getStudentName(), student.getAge()});
			
			for(var cid : student.getCourses())
				jdbcTemplate.update(sql2, student.getRollNo(), cid);
		}
		
		jdbcTemplate.batchUpdate(sql, args);
	}
	
	public List<Student> getAllStudents() {
		sql = "select * from students";
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	public List<Integer> getStudentCourses(int rollNo) {
		sql = "select ID from students_courses where Roll_No=?";
		
		return jdbcTemplate.query(sql, (RowMapper<Integer>) (arg0, arg1) -> Integer.parseInt(arg0.getObject("ID").toString()), rollNo);
	}
	
	public void updateStudent(int id, Student student) {
		sql = "update students set Roll_No=?, StudentName=?, Age=? where Roll_No=?";
		
		jdbcTemplate.update(sql, student.getRollNo(), student.getStudentName(), student.getAge(), id);
		jdbcTemplate.update("delete from students_courses where Roll_No=?", id);
		
		sql = "insert into students_courses values(?, ?)";
		
		if(student.getCourses() != null)
			for(Course course : student.getCourses())
				jdbcTemplate.update(sql, student.getRollNo(), course.getCourseID());
		
	}
	
	public void deleteStudent(int id) {
		sql = "delete from students where Roll_No=?";
		jdbcTemplate.update(sql, id);
		
		sql = "delete from students_courses where Roll_No=?";
		jdbcTemplate.update(sql, id);
	}
	
	public void clear() {
		sql = "truncate table students";
		jdbcTemplate.execute(sql);
	}
}
