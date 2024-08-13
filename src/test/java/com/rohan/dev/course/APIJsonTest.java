package com.rohan.dev.course;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.rohan.dev.course.dto.Course;

@SpringBootTest
public class APIJsonTest {

	@Autowired
	RestTemplate restTemplate;
	
	@Test
	void checkJsonFormat() {
		ResponseEntity<Course> expected = restTemplate.getForEntity(URI.create("/api/courses/2"), Course.class);
		assertThat(expected.getBody()).isNotNull();
	}
}
