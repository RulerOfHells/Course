package com.rohan.dev.course.repository.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.rohan.dev.course.domain.User;

// userID as id, firstName, lastName, email, password, address, phone, enabled, imageURL as imgURL, usingMFA as isUsingMFA, nonLocked as isNotLocked

@Component
public class UserRowMapper implements RowMapper<User> {

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		return new User()
				.setId(rs.getLong("userID"))
				.setFirstName(rs.getString("firstName"))
				.setLastName(rs.getString("lastName"))
				.setEmail(rs.getString("email"))
				.setPassword(rs.getString("password"))
				.setAddress(rs.getString("address"))
				.setPhone(rs.getString("phone"))
				.setEnabled((rs.getInt("enabled") == 1)? true: false)
				.setImgURL(rs.getString("imageURL"))
				.setUsingMFA((rs.getInt("usingMFA") == 1)? true: false)
				.setNotLocked((rs.getInt("nonLocked") == 1)? true: false);
	}

}
