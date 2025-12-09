package com.rohan.dev.course.repository.impl;

import com.rohan.dev.course.domain.Role;
import com.rohan.dev.course.domain.User;
import com.rohan.dev.course.domain.UserPrinciple;
import com.rohan.dev.course.dto.UserDTO;
import com.rohan.dev.course.exceptions.ApiException;
import com.rohan.dev.course.repository.RoleRepository;
import com.rohan.dev.course.repository.UserRepository;
import com.rohan.dev.course.service.EmailService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static com.rohan.dev.course.enumeration.RoleType.ROLE_USER;
import static com.rohan.dev.course.enumeration.VerificationType.ACCOUNT;
import static com.rohan.dev.course.repository.query.UserQuery.*;
import static java.util.Objects.requireNonNull;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository<User>, UserDetailsService {
	
	private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	@Autowired
	private RoleRepository<Role> roleRepository;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private EmailService emailService;
	@Autowired
	private RowMapper<User> rowMapper;
	
	private final Logger logs = Logger.getLogger(UserRepositoryImpl.class.getName());
	
	@Override
	public User create(User user) {
		if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0)				//check for duplicate
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


	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = getUserByEmail(email);
		if(user == null) {
			logs.warning("User not found");
			throw new UsernameNotFoundException("User not found");
		} else {
			logs.info("User match found in database: "+email);
			return new UserPrinciple(user, roleRepository.getRoleByUserId(user.getId()).getPerms());
		}
	}
	
	@Override
	public User getUserByEmail(String email) {
		try {
            return jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL_QUERY, Map.of("email", email), rowMapper);
		}
		catch(EmptyResultDataAccessException e) {
            logs.warning("User not found with email: "+email);
			throw new UsernameNotFoundException("User not found with email: "+email);
		}
		catch(Exception e) {
			logs.severe(e.getMessage());
			throw new ApiException("Something went wrong. Try again");
		}
	}


	@Override
	public void sendVerificationCode(UserDTO userDTO) {
		String expirationDate = DateFormatUtils.format(DateUtils.addDays(new Date(), 1), DATE_FORMAT);
		String verificationCode = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
		
		try {
			jdbcTemplate.update(DELETE_MFA_CODE_QUERY, Map.of("userID", userDTO.getId()));
			jdbcTemplate.update(INSERT_MFA_CODE_QUERY, Map.of("userID", userDTO.getId(), "code", verificationCode, "expDate", expirationDate));
			sendEmail(userDTO.getEmail(), "From: CourseManager \nYour MFA code is "+verificationCode);
		}
		catch(Exception e) {
			logs.severe(e.getMessage());
			throw new ApiException("Something went wrong. Try again");
		}
	}
	
	private void sendEmail(String email, String message) {
		emailService.sendMail(email, "MFA code", "Hi! you tried to login at "+LocalDateTime.now()+"\n"+message);
	}
}
