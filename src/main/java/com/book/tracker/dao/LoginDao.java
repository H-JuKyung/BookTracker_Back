package com.book.tracker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.book.tracker.dto.Login;

@Mapper
public interface LoginDao {
    public void insertToken(Login login) throws Exception;

    public void deleteToken(@Param("token") String token) throws Exception;

    public Login getLoginInfo(@Param("token") String token) throws Exception;

    public void updateExpTime(@Param("token") String token, @Param("newExpTime") long newExpTime) throws Exception;
    
    String getUserEmailByToken(@Param("token") String token);
}