//package com.rohan.dev.course.services;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import com.rohan.dev.course.dto.Course;
//import com.rohan.dev.course.dto.Student;
//
//@Service
//public class RegistrationService {
//
//	private StudentService studentService;
//	private CourseService courseService;
//	
//	@Autowired
//	public RegistrationService(StudentService studentService, CourseService courseService) {
//		this.studentService = studentService;
//		this.courseService = courseService;
//	}
//	
//	public Student getEnrolledStudent(int roll) throws IllegalArgumentException {
//		Student s = studentService.getStudentById(roll);
//		List<Course> s_courses = new ArrayList<>();
//		for(Integer i : studentService.getStudentCourses(roll))
//			s_courses.add(courseService.getCourse(i));
//		s.setCourses(s_courses);
//		return s;
//	}
//	
//	public List<Student> getAllEnrolledStudents() throws IllegalStateException {
//		List<Student> students = studentService.getAllStudents();
//		for(var student : students)
//			student.setCourses(getEnrolledStudent(student.getRollNo()).getCourses());
//		return students;
//	}
//	
//	public void unassignCourse(int courseID) {
//		studentService.unassignCourseFromAllStudents(courseID);
//	}
//	
//}
