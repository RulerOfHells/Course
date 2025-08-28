package com.rohan.dev.course.exceptions;

public class InvalidStudentException extends RuntimeException{
	public InvalidStudentException(String msg) {
		super(msg);
	}
}