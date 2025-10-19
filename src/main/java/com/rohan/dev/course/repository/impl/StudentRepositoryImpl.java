package com.rohan.dev.course.repository.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.rohan.dev.course.domain.Student;
import com.rohan.dev.course.repository.StudentRepository;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

	private JdbcTemplate jdbcTemplate;
	private BeanPropertyRowMapper<Student> rowMapper;
	private String sql;
	
	@Autowired
	public StudentRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = BeanPropertyRowMapper.newInstance(Student.class);
	}
	
	@Override
	public void addStudent(Student student) {
		sql = "insert into Students values(?, ?, ?)";
		
		jdbcTemplate.update(sql, student.getRollNo(), student.getStudentName(), student.getAge());
		
		if(student.getCourses() == null) return;
		
		sql = "insert into Students_courses values(?, ?)";
		
		for(var cid : student.getCourses())
			jdbcTemplate.update(sql, student.getRollNo(), cid);
	}
	
	@Override
	public void batchAdd(List<Student> students) {
		sql = "insert into Students values(?, ?, ?)";
		String sql2 = "insert into Students_courses values(?, ?)";
		
		List<Object[]> args = new LinkedList<>();
		
		for(var student : students) {
			args.add(new Object[] {student.getRollNo(), student.getStudentName(), student.getAge()});
			
			for(var cid : student.getCourses())
				jdbcTemplate.update(sql2, student.getRollNo(), cid);
		}
		
		jdbcTemplate.batchUpdate(sql, args);
	}
	
	@Override
	public List<Student> getAllStudents() {
		sql = "select * from Students";
		return jdbcTemplate.query(sql, rowMapper);
	}
	
	@Override
	public Student getStudent(int rollNo) {
		sql = "select rollNo, studentName, age from Students where rollNo=?";
		List<Student> students = jdbcTemplate.query(sql, rowMapper, rollNo);
		if(students.size() == 0)
			return null;
		return students.get(0);
	}
	
	@Override
	public List<Integer> getStudentCourses(int rollNo) {
		sql = "select courseID from Students_courses where rollNo=?";
		
		return jdbcTemplate.query(sql, (RowMapper<Integer>) (arg0, arg1) -> Integer.parseInt(arg0.getObject("courseID").toString()), rollNo);
	}
	
	@Override
	public void unassignCourseFromAllStudents(int courseId) {
	    String sql = "DELETE FROM Students_courses WHERE courseID = ?";
	    jdbcTemplate.update(sql, courseId);
	}
	
	@Override
	public void updateStudent(int id, Student student) {
		sql = "update Students set rollNo=?, studentName=?, age=? where rollNo=?";
		
		jdbcTemplate.update(sql, student.getRollNo(), student.getStudentName(), student.getAge(), id);
		jdbcTemplate.update("delete from students_courses where rollNo=?", id);
		
		sql = "insert into Students_courses values(?, ?)";
		
		if(student.getCourses() != null)
			for(Integer cid : student.getCourses())
				jdbcTemplate.update(sql, student.getRollNo(), cid);
		
	}
	
	@Override
	public void deleteStudent(int id) {
		sql = "delete from Students_courses where rollNo=?";
		jdbcTemplate.update(sql, id);
		
		sql = "delete from Students where rollNo=?";
		jdbcTemplate.update(sql, id);
	}
}
