package com.rohan.dev.course.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohan.dev.course.domain.User;
import com.rohan.dev.course.dto.UserDTO;
import com.rohan.dev.course.dtomapper.UserDTOMapper;
import com.rohan.dev.course.repository.UserRepository;
import com.rohan.dev.course.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository<User> userRepository;
	
	@Override
	public UserDTO createUser(User user) {
		return UserDTOMapper.fromUser(userRepository.create(user));
	}
	
	@Override
	public UserDTO getUserFromEmail(String email) {
		return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
	}

	@Override
	public void sendVerificationCode(UserDTO userDTO) {
		userRepository.sendVerificationCode(userDTO);
	}

}
