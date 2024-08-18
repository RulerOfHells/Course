package com.rohan.dev.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import com.rohan.dev.course.dto.Course;

@SpringBootTest
public class APIJsonTest {

	TestRestTemplate restTemplate = new TestRestTemplate();
	
	@Test
	void checkJsonFormat() {
		ResponseEntity<Course> expected = restTemplate.getForEntity(URI.create("http://localhost:80/api/courses/2"), Course.class);
		assertThat(expected.getBody()).isNotNull();
	}
}
