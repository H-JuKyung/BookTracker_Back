package com.book.tracker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.book.tracker.dto.Login;

@Mapper
public interface LoginDao {
    
    // ✅ 로그인 성공 시 토큰 및 만료 시간 저장
    public void insertToken(Login login) throws Exception;

    // ✅ 로그아웃 시 토큰 삭제 (삭제 전 존재 여부 확인 필요)
    public void deleteToken(@Param("token") String token) throws Exception;

    // ✅ 토큰을 통해 로그인 정보 조회 (만료 시간 확인용)
    public Login getLoginInfo(@Param("token") String token) throws Exception;

    // ✅ 만료 시간(exp) 업데이트 (유효한 토큰인지 먼저 검사 필요)
    public void updateExpTime(@Param("token") String token, @Param("newExpTime") long newExpTime) throws Exception;
}