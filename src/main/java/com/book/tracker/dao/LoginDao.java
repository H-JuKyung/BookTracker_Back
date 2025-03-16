package com.book.tracker.dao;

import org.apache.ibatis.annotations.Mapper;

import com.book.tracker.dto.Login;

@Mapper
public interface LoginDao {
	
	public void insertToken(Login login) throws Exception;
	
	public void deleteToken(String token) throws Exception;
	
	public Login checkToken(String authorization) throws Exception;
}
