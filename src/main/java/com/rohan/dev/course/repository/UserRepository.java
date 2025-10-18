package com.rohan.dev.course.repository;

import java.util.Collection;

import com.rohan.dev.course.domain.User;

public interface UserRepository<T extends User> {
	
	T create(T data);
	Collection<T> list(int page, int pageSize);
	T get(long id);
	T update(T data);
	boolean delete(long id);
}
