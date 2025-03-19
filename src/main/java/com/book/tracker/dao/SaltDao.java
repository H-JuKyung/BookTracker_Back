package com.book.tracker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.book.tracker.dto.SaltInfo;

@Mapper
public interface SaltDao {

    // ✅ 새로운 Salt 저장 (중복 여부 확인 후 삽입 필요)
    public void insertSalt(SaltInfo saltInfo) throws Exception;

    // ✅ 이메일을 통해 Salt 조회 (회원 로그인 시 필요)
    public SaltInfo selectSalt(@Param("email") String email) throws Exception;
}
