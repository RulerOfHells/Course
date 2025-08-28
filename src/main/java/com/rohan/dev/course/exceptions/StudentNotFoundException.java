package com.rohan.dev.course.exceptions;

public final class StudentNotFoundException extends RuntimeException {
	public StudentNotFoundException(String msg) {
		super(msg);
	}
}