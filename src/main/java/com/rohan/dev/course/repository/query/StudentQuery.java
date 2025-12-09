package com.rohan.dev.course.repository.query;

public class StudentQuery {
    public static final String SELECT_ALL_STUDENTS_QUERY = "SELECT * FROM Students";
    public static final String SELECT_SINGLE_STUDENT_QUERY = "SELECT * FROM Students WHERE rollNo = ?";
    public static final String SELECT_STUDENT_COURSES_QUERY = "SELECT courseID FROM Students_courses WHERE rollNo = ?";
    public static final String INSERT_STUDENT_QUERY = "INSERT INTO Students VALUES(?, ?, ?)";
    public static final String INSERT_STUDENT_COURSES_QUERY = "INSERT INTO Students_courses VALUES(?, ?)";
    public static final String UPDATE_STUDENT_QUERY = "UPDATE Students SET rollNo = ?, studentName = ?, age = ? WHERE rollNo=?";
    public static final String DELETE_STUDENT_QUERY = "DELETE FROM Students WHERE rollNo = ?";
    public static final String DELETE_STUDENT_COURSES_WITH_CID_QUERY = "DELETE FROM Students_courses WHERE courseID = ?";
    public static final String DELETE_STUDENT_COURSES_WITH_ROLL_QUERY = "DELETE FROM Students_courses WHERE rollNo = ?";
}
