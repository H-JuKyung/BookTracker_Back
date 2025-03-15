package com.book.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.tracker.dao.UserDao;
import com.book.tracker.dto.User;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	public void insertUser(User u) throws Exception {
		userDao.insertUser(u);
	}
}
