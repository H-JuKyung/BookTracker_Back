package com.book.tracker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.book.tracker.dto.SaltInfo;

@Mapper
public interface SaltDao {

    public void insertSalt(SaltInfo saltInfo) throws Exception;

    public SaltInfo selectSalt(@Param("email") String email) throws Exception;
}
