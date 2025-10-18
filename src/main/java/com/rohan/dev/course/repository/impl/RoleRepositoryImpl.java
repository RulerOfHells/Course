package com.rohan.dev.course.repository.impl;

import static com.rohan.dev.course.enumeration.RoleType.ROLE_USER;
import static com.rohan.dev.course.repository.query.RoleQuery.*;
import static java.util.Objects.requireNonNull;

import java.util.logging.Logger;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rohan.dev.course.domain.Role;
import com.rohan.dev.course.exceptions.ApiException;
import com.rohan.dev.course.repository.RoleRepository;

@Repository
public class RoleRepositoryImpl implements RoleRepository<Role>{
	
	private BeanPropertyRowMapper<Role> rowMapper = BeanPropertyRowMapper.newInstance(Role.class);

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	
	private final Logger logs = Logger.getLogger("logs.txt");
	
	@Override
	public Role create(Role data) {
		return null;
	}

	@Override
	public Collection<Role> list(int page, int pageSize) {
		return null;
	}

	@Override
	public Role get(long id) {
		return null;
	}

	@Override
	public Role update(Role data) {
		return null;
	}

	@Override
	public boolean delete(long id) {
		return false;
	}

	@Override
	public void addRoleToUser(long userID, String roleName) {
		logs.info("Adding role "+roleName+" to user ID: "+userID);
		try {
			Role role = jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME_QUERY, Map.of("role", roleName), rowMapper);
			logs.info("RoleID :"+role.getRoleID() + " name : " + role.getName() + " perms " + role.getPerms());
			jdbcTemplate.update(INSERT_ROLE_TO_USER_QUERY, Map.of("userID", userID, "roleID", requireNonNull(role).getRoleID()));
		}
		catch(EmptyResultDataAccessException e) {
			throw new ApiException("Role " + ROLE_USER.name() +" not found!");
		}
		catch(Exception e) {
			logs.severe(e.getMessage());
			throw new ApiException("Something went wrong. Try again");
		}
	}

	@Override
	public Role getRoleByUserId(long userID) {
		return null;
	}

	@Override
	public Role getRoleByUserEmail(String email) {
		return null;
	}

	@Override
	public void updateUserRole(long userID, String roleName) {
	}

}
