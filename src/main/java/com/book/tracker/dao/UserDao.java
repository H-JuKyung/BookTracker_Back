package com.book.tracker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.book.tracker.dto.User;

@Mapper
public interface UserDao {

    public User login(User u) throws Exception;
    
    public void insertUser(User u) throws Exception;
    
    public void updateUser(User u) throws Exception;
    
    public void deleteUser(@Param("email") String email) throws Exception;
    
    public User useByNickname(@Param("nickname") String nickname);
}

