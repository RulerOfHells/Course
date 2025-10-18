package com.rohan.dev.course.repository.impl;

import static com.rohan.dev.course.enumeration.RoleType.ROLE_USER;
import static com.rohan.dev.course.enumeration.VerificationType.ACCOUNT;
import static com.rohan.dev.course.repository.query.UserQuery.COUNT_EMAIL_QUERY;
import static com.rohan.dev.course.repository.query.UserQuery.INSERT_ACCOUNT_VERIFICATION_URL_QUERY;
import static com.rohan.dev.course.repository.query.UserQuery.INSERT_USER_QUERY;
import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rohan.dev.course.domain.Role;
import com.rohan.dev.course.domain.User;
import com.rohan.dev.course.exceptions.ApiException;
import com.rohan.dev.course.repository.RoleRepository;
import com.rohan.dev.course.repository.UserRepository;
import com.rohan.dev.course.service.EmailService;

@Repository
public class UserRepositoryImpl implements UserRepository<User>{
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	@Autowired
	private RoleRepository<Role> roleRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private EmailService emailService;
	
	private final Logger logs = Logger.getLogger("logs.txt");
	
	@Override
	public User create(User user) {		if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0)				//check for duplicate
			throw new ApiException("Email in use. Please use a different one");
		
		try {
			KeyHolder holder = new GeneratedKeyHolder();
			SqlParameterSource parameters = getSqlParameters(user);
			jdbcTemplate.update(INSERT_USER_QUERY, parameters, holder);		//user inserted and key (id) retrieved
			
			user.setId(requireNonNull(holder.getKey().longValue()));
			roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());	//role added
			
			String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
			
			jdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, Map.of("userID", user.getId(), "url", verificationUrl));		//verification url generated and saved
			emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT.getType());
			
			user.setEnabled(false);		//Initially new user will not be enabled
			
			user.setNotLocked(true);	//Not locked since it's new and no repeated incorrect login attempts
			
			return user;
		}
		catch(Exception e) {
			logs.severe(e.getMessage());
			throw new ApiException("Something went wrong. Try again");
		}
	}

	
	@Override
	public Collection<User> list(int page, int pageSize) {
		return null;
	}

	@Override
	public User get(long id) {
		return null;
	}

	@Override
	public User update(User data) {
		return null;
	}

	@Override
	public boolean delete(long id) {
		return false;
	}
	
	private int getEmailCount(String email) {
		return jdbcTemplate.queryForObject(COUNT_EMAIL_QUERY, Map.of("email", email), Integer.class);
	}
	
	private SqlParameterSource getSqlParameters(User user) {
		return new MapSqlParameterSource()
				.addValue("firstName", user.getFirstName())
				.addValue("lastName", user.getLastName())
				.addValue("email", user.getEmail())
				.addValue("password", encoder.encode(user.getPassword()))
				.addValue("phone", user.getPhone());
	}
	
	private String getVerificationUrl(String key, String type) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
	}
}
