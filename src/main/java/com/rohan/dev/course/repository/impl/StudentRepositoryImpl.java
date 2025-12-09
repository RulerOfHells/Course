package com.rohan.dev.course.repository.impl;

import com.rohan.dev.course.domain.Student;
import com.rohan.dev.course.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static com.rohan.dev.course.repository.query.StudentQuery.*;

@Repository
@Transactional
public class StudentRepositoryImpl implements StudentRepository {

	private final JdbcTemplate jdbcTemplate;
	private final BeanPropertyRowMapper<Student> rowMapper;
	
	@Autowired
	public StudentRepositoryImpl(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = BeanPropertyRowMapper.newInstance(Student.class);
	}
	
	@Override
	public void addStudent(Student student) {
		jdbcTemplate.update(INSERT_STUDENT_QUERY, student.getRollNo(), student.getStudentName(), student.getAge());
		
		if(student.getCourses() == null) return;

		for(var cid : student.getCourses())
			jdbcTemplate.update(INSERT_STUDENT_COURSES_QUERY, student.getRollNo(), cid);
	}
	
	@Override
	public void batchAdd(List<Student> students) {
		List<Object[]> args = new LinkedList<>();
		
		for(var student : students) {
			args.add(new Object[] {student.getRollNo(), student.getStudentName(), student.getAge()});
			
			for(var cid : student.getCourses())
				jdbcTemplate.update(INSERT_STUDENT_COURSES_QUERY, student.getRollNo(), cid);
		}
		
		jdbcTemplate.batchUpdate(INSERT_STUDENT_QUERY, args);
	}
	
	@Override
	public List<Student> getAllStudents() {
		return jdbcTemplate.query(SELECT_ALL_STUDENTS_QUERY, rowMapper);
	}
	
	@Override
	public Student getStudent(int rollNo) {
		List<Student> students = jdbcTemplate.query(SELECT_SINGLE_STUDENT_QUERY, rowMapper, rollNo);
		if(students.isEmpty())
			return null;
		return students.get(0);
	}
	
	@Override
	public List<Integer> getStudentCourses(int rollNo) {
		return jdbcTemplate.query(SELECT_STUDENT_COURSES_QUERY, (arg0, arg1) -> Integer.parseInt(arg0.getObject("courseID").toString()), rollNo);
	}
	
	@Override
	public void unassignCourseFromAllStudents(int courseId) {
	    jdbcTemplate.update(DELETE_STUDENT_COURSES_WITH_CID_QUERY, courseId);
	}
	
	@Override
	public void updateStudent(int id, Student student) {
		jdbcTemplate.update(UPDATE_STUDENT_QUERY, student.getRollNo(), student.getStudentName(), student.getAge(), id);
		jdbcTemplate.update(DELETE_STUDENT_COURSES_WITH_ROLL_QUERY, id);

		if(student.getCourses() != null)
			for(Integer cid : student.getCourses())
				jdbcTemplate.update(INSERT_STUDENT_COURSES_QUERY, student.getRollNo(), cid);
		
	}
	
	@Override
	public void deleteStudent(int id) {
		jdbcTemplate.update(DELETE_STUDENT_COURSES_WITH_ROLL_QUERY, id);
		jdbcTemplate.update(DELETE_STUDENT_QUERY, id);
	}
}
