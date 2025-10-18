package com.rohan.dev.course.repository;

import java.util.Collection;

import com.rohan.dev.course.domain.Role;

public interface RoleRepository<T extends Role> {
	
	T create(T data);
	Collection<T> list(int page, int pageSize);
	T get(long id);
	T update(T data);
	boolean delete(long id);
	
	void addRoleToUser(long userID, String roleName);
	T getRoleByUserId(long userID);
	T getRoleByUserEmail(String email);
	void updateUserRole(long userID, String roleName);
}
