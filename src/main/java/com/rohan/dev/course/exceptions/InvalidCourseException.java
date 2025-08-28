package com.rohan.dev.course.exceptions;

public class InvalidCourseException extends RuntimeException {
	public InvalidCourseException(String msg) {
		super(msg);
	}
}