package com.rohan.dev.course.repository.query;

public class UserQuery {
	public static final String INSERT_USER_QUERY = "INSERT INTO Users(firstName, lastName, email, password, phone) VALUES(:firstName, :lastName, :email, :password, :phone)";
	public static final String COUNT_EMAIL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
	public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications(userID, url, expiryDate) VALUES (:userID, :url, DATE_ADD(NOW(), INTERVAL 1 YEAR))";
}
