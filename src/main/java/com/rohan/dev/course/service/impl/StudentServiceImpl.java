package com.rohan.dev.course.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.dto.StudentDTO;
import com.rohan.dev.course.dtomapper.StudentDTOMapper;
import com.rohan.dev.course.repository.StudentRepository;
import com.rohan.dev.course.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {
	
	private StudentRepository studentDAO;
	
	@Autowired
	public StudentServiceImpl(StudentRepository studentDAO) {
		this.studentDAO = studentDAO;
	}
	
	private StudentDTO getStudentById(int id) throws IllegalArgumentException {
		StudentDTO s = StudentDTOMapper.fromStudent(studentDAO.getStudent(id));
		if(s == null)
			throw new IllegalArgumentException();
		s.setCourses(getStudentCourses(id));
		return s;
	}
	
	private boolean exists(int id) {
		try {
			getStudentById(id);
		}
		catch(IllegalArgumentException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public StudentDTO getStudent(int rollNo) {
		if(!exists(rollNo))
			throw new IllegalArgumentException();
		return getStudentById(rollNo);
	}
	
	@Override
	public List<StudentDTO> getAllStudents() throws IllegalStateException {
		List<StudentDTO> students = StudentDTOMapper.fromStudentList(studentDAO.getAllStudents());
		if(students == null)
			throw new IllegalStateException();
		for(StudentDTO student : students)
			student.setCourses(getStudentCourses(student.getRollNo()));
		return students;
	}
	
	
	@Override
	public List<Integer> getStudentCourses(int roll) {
		return studentDAO.getStudentCourses(roll);
	}
	
	@Override
	public void addStudent(StudentDTO student) throws IllegalArgumentException {
		if(exists(student.getRollNo()))
			throw new IllegalArgumentException();
		studentDAO.addStudent(StudentDTOMapper.toStudent(student));
	}
	
	@Override
	public void updateStudent(int id, StudentDTO newStudent) throws IllegalArgumentException, IllegalStateException {
		if(exists(newStudent.getRollNo()) && id != newStudent.getRollNo())
			throw new IllegalStateException();
		StudentDTO student = getStudentById(id);
		student.setAge(newStudent.getAge());
		student.setStudentName(newStudent.getStudentName());
		student.setRollNo(newStudent.getRollNo());
		student.setCourses(newStudent.getCourses());
			
		studentDAO.updateStudent(id, StudentDTOMapper.toStudent(student));
	}
	
	@Override
	public StudentDTO removeStudent(int id) throws IllegalArgumentException {
		if(!exists(id))
			throw new IllegalStateException();
		StudentDTO student = getStudentById(id);
		studentDAO.deleteStudent(id);
		return student;
	}
	
	@Override
	public void unassignCourseFromAllStudents(int courseId) {
        studentDAO.unassignCourseFromAllStudents(courseId);
    }
}
