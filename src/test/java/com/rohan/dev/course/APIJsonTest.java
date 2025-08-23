package com.rohan.dev.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.rohan.dev.course.dto.Course;
import com.rohan.dev.course.dto.Student;

@SpringBootTest
public class APIJsonTest {

	TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	void checkJsonFormatForCourse() {
		ResponseEntity<Course> expected = restTemplate.getForEntity(URI.create("http://localhost:80/api/courses/2"), Course.class);
		assertThat(expected.getBody()).isNotNull();
		assertThat(expected.getBody()).hasFieldOrProperty("courseID");
		assertThat(expected.getBody()).hasFieldOrProperty("courseName");
	}
	
	@Test
	void checkJsonFormatForStudent() {
		ResponseEntity<Student> expected = restTemplate.getForEntity(URI.create("http://localhost:80/api/students/1"), Student.class);
		assertThat(expected.getBody()).isNotNull();
		assertThat(expected.getBody()).hasFieldOrProperty("rollNo");
		assertThat(expected.getBody()).hasFieldOrProperty("StudentName");
		assertThat(expected.getBody()).hasFieldOrProperty("age");
		assertThat(expected.getBody()).hasFieldOrProperty("courses");
	}
}
