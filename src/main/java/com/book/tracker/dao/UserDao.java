package com.book.tracker.dao;

import java.sql.Connection;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.book.tracker.dto.User;

@Mapper
public interface UserDao {

	public User login(User u) throws Exception;
	
	public void insertUser(User u) throws Exception;
	
	public void updateUser(User u) throws Exception;
	
	public void deleteUser(String email) throws Exception;
	
    public User useByNickname(@Param("nickname") String nickname);
}

