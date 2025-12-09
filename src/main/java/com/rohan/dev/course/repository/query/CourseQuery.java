package com.rohan.dev.course.repository.query;

public class CourseQuery {
    public static final String INSERT_COURSE_QUERY = "INSERT INTO Courses VALUES(?, ?)";
    public static final String SELECT_ALL_COURSE_QUERY = "SELECT courseID, courseName FROM Courses";
    public static final String SELECT_SINGLE_COURSE_QUERY = "SELECT courseID, courseName FROM Courses WHERE courseID = ?";
    public static final String UPDATE_COURSE_QUERY = "UPDATE Courses SET courseID = ?, courseName = ? WHERE courseID = ?";
    public static final String DELETE_COURSE_QUERY = "DELETE FROM Courses WHERE courseID = ?";
}
