package com.rohan.dev.course.service;

import com.rohan.dev.course.domain.User;
import com.rohan.dev.course.dto.UserDTO;

public interface UserService {
	UserDTO createUser(User user);
}
