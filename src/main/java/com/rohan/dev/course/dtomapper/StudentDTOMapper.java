package com.rohan.dev.course.dtomapper;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.rohan.dev.course.domain.Student;
import com.rohan.dev.course.dto.StudentDTO;

public class StudentDTOMapper {
	public static StudentDTO fromStudent(Student student) {
		if(student == null) return null;
		
		StudentDTO studentDTO = new StudentDTO();
		BeanUtils.copyProperties(student, studentDTO);
		return studentDTO;
	}
	
	public static Student toStudent(StudentDTO studentDTO) {
		if(studentDTO == null) return null;
		
		Student student = new Student();
		BeanUtils.copyProperties(studentDTO, student);
		return student;
	}
	
	public static List<StudentDTO> fromStudentList(List<Student> students) {
		if(students == null) return null;
		
		return students.stream().map(StudentDTOMapper::fromStudent).toList();
	}
	
	public static List<Student> toStudentList(List<StudentDTO> students) {
		if(students == null) return null;
		
		return students.stream().map(StudentDTOMapper::toStudent).toList();
	}
}
