package com.book.tracker.service;

import java.util.Date; 
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.book.tracker.dao.LoginDao;
import com.book.tracker.dao.SaltDao;
import com.book.tracker.dao.UserDao;
import com.book.tracker.dto.Login;
import com.book.tracker.dto.SaltInfo;
import com.book.tracker.dto.User;
import com.book.tracker.util.OpenCrypt;

@Service
public class UserService {
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	LoginDao loginDao;
	
	@Autowired
	SaltDao saltDao;
	
	// ✅ 토큰을 검사하여 로그인 상태 확인
	public Login getLoginInfo(String authorization) throws Exception {
		return loginDao.getLoginInfo(authorization);
	}
	
	// ✅ 로그인 시 토큰과 만료 시간(exp) 저장
	public Login tokenLogin(User u) throws Exception {
		String email = u.getEmail();
		
        // 1️. 이메일로 salt 조회
        SaltInfo saltInfo = saltDao.selectSalt(email);
        if (saltInfo == null) {
            throw new Exception("등록되지 않은 이메일입니다.");
        }
        
        // 2️. 패스워드 암호화 후 로그인 시도
		String pwd = u.getPwd();
		byte [] pwdHash = OpenCrypt.getSHA256(pwd, saltInfo.getSalt());
		String pwdHashHex = OpenCrypt.byteArrayToHex(pwdHash);
		u.setPwd(pwdHashHex);
		u = userDao.login(u);		
		
		if(u != null) {
			String nickname = u.getNickname();
			if(nickname != null && !nickname.trim().equals("")) {
				// ✅ 로그인 성공 -> 토큰 생성 및 저장		
				
				/* 3. 새로운 토큰 생성*/
				// 3-1. salt를 생성
				String salt = UUID.randomUUID().toString();
				// 3-2. email을 hashing 
				byte[] originalHash = OpenCrypt.getSHA256(email, salt);
				// 3-3. DB에 저장하기 좋은 포맷으로 인코딩
				String myToken = OpenCrypt.byteArrayToHex(originalHash);
				
				// 4️. 만료 시간(exp) 설정 (현재 시간 + 30분)
                long expTime = System.currentTimeMillis() + (30 * 60 * 1000);
                
				// 5. login table에 token 저장
				Login loginInfo = new Login(email, myToken, nickname, new Date(), expTime);
				loginDao.insertToken(loginInfo);
				
				return loginInfo;
			}
		}
		
		return null;		 
	}
	
	// ✅ 회원가입 기능
	public void insertUser(User u) throws Exception{
		
		// 이메일 유효성 검사
		String email = u.getEmail();
	    if (!isValidEmail(email)) {
	        throw new Exception("유효하지 않은 이메일 형식입니다.");
	    }		
		
		// 패스워드 유효성 검사
	    String pwd = u.getPwd();
	    if (!isValidPassword(pwd)) {
	        throw new Exception("패스워드는 8자리 이상이어야 하며, 특수문자와 숫자를 포함해야 합니다.");
	    }
	    
	    // 패스워드 암호화
	    //1. salt를 생성
		String salt = UUID.randomUUID().toString();
		//2. pwd를 hashing
		byte[] originalHash = OpenCrypt.getSHA256(pwd, salt);
		//3. db에 저장하기 좋은 포맷으로 인코딩
		String pwdHash = OpenCrypt.byteArrayToHex(originalHash);
		u.setPwd(pwdHash);
	    
		saltDao.insertSalt(new SaltInfo(email, salt));
		userDao.insertUser(u);
	}
	
	// ✅ 사용자 정보 업데이트
	public void updateUser(User u) throws Exception{
		userDao.updateUser(u);
	}
	
	// ✅ 사용자 삭제
	public void deleteUser(String email) throws Exception{
		userDao.deleteUser(email);
	}
	
	// ✅ 로그아웃 (토큰 삭제)
    public void logout(String authorization) throws Exception {
        if (authorization == null) {
            System.out.println("⚠️ 로그아웃 요청 거부 - 토큰 없음");
            return;
        }

        loginDao.deleteToken(authorization);
        System.out.println("✅ 로그아웃 완료: " + authorization);
    }
	
    // ✅ 사용자의 닉네임 중복 확인
	public boolean isNicknameAvailable(String nickname) {
		User existingUser = userDao.useByNickname(nickname);
		return existingUser == null; 
	}
	
    // ✅ 요청할 때마다 exp(만료 시간) 갱신 (현재 시간 + 30분)
	public void updateExpTime(String token, long newExpTime) throws Exception {
	    if (token == null) {
	        System.out.println("⚠️ 로그인 시간 갱신 요청 거부 - 토큰 없음");
	        return;
	    }

	    // 현재 저장된 토큰 정보 가져오기
	    Login loginInfo = loginDao.getLoginInfo(token);
	    
	    if (loginInfo != null) {
	        long currentTime = System.currentTimeMillis();
	        
	        // 🔹 현재 시간이 기존 만료 시간보다 작을 때만 갱신
	        if (currentTime < loginInfo.getExp()) {
	            loginDao.updateExpTime(token, newExpTime);
	            System.out.println("✅ 토큰 만료 시간 갱신됨 - 새로운 만료 시간: " + newExpTime);
	        } else {
	            System.out.println("⏳ 토큰 만료 시간이 지남 - 갱신하지 않음.");
	        }
	    } else {
	        System.out.println("❌ 유효하지 않은 토큰입니다.");
	    }
	}
    
    // ✅ 이메일 유효성 검사
	private boolean isValidEmail(String email) {
	    // 이메일 패턴
	    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    return Pattern.matches(emailPattern, email);
	}
	
	// ✅ 패스워드 유효성 검사
	private boolean isValidPassword(String password) {
	    // 패스워드 패턴: 8자리 이상, 숫자 포함, 특수문자 포함
	    String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
	    return Pattern.matches(passwordPattern, password);
	}
}