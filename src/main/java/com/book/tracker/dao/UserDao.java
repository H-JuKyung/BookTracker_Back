package com.book.tracker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.book.tracker.dto.User;

@Mapper
public interface UserDao {

    // ✅ 로그인 기능 (email + pwd 일치하는 사용자 조회)
    public User login(User u) throws Exception;
    
    // ✅ 신규 사용자 등록
    public void insertUser(User u) throws Exception;
    
    // ✅ 사용자 정보 업데이트
    public void updateUser(User u) throws Exception;
    
    // ✅ 사용자 삭제 (삭제 전 존재 여부 확인 필요)
    public void deleteUser(@Param("email") String email) throws Exception;
    
    // ✅ 닉네임 중복 확인 (중복이면 User 객체 반환, 없으면 null)
    public User useByNickname(@Param("nickname") String nickname);
}

