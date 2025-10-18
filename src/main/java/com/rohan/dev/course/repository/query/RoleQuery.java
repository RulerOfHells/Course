package com.rohan.dev.course.repository.query;

public class RoleQuery {
	public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO User_Roles (userID, roleID) VALUES (:userID, :roleID)";
	public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :role";
}
