package com.rohan.dev.course.editors;

import java.beans.PropertyEditorSupport;

import com.rohan.dev.course.dto.Course;

public class StudentCoursesEditor extends PropertyEditorSupport {
		
	@Override
	public void setAsText(String text) {
		String[] id_name = text.split("\\+");
		
		setValue(new Course(Integer.parseInt(id_name[0]), id_name[1]));
	}
}
