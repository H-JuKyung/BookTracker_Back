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
	
	public Login getLoginInfo(String authorization) throws Exception {
		return loginDao.getLoginInfo(authorization);
	}
	
	public Login tokenLogin(User u) throws Exception {
		String email = u.getEmail();
		
        SaltInfo saltInfo = saltDao.selectSalt(email);
        if (saltInfo == null) {
            throw new Exception("등록되지 않은 이메일입니다.");
        }
        
		String pwd = u.getPwd();
		byte [] pwdHash = OpenCrypt.getSHA256(pwd, saltInfo.getSalt());
		String pwdHashHex = OpenCrypt.byteArrayToHex(pwdHash);
		u.setPwd(pwdHashHex);
		u = userDao.login(u);		
		
		if(u != null) {
			String nickname = u.getNickname();
			if(nickname != null && !nickname.trim().equals("")) {
				
				String salt = UUID.randomUUID().toString();
				byte[] originalHash = OpenCrypt.getSHA256(email, salt);
				String myToken = OpenCrypt.byteArrayToHex(originalHash);
				
                long expTime = System.currentTimeMillis() + (30 * 60 * 1000);
                
				Login loginInfo = new Login(email, myToken, nickname, new Date(), expTime);
				loginDao.insertToken(loginInfo);
				
				return loginInfo;
			}
		}
		
		return null;		 
	}
	
	public void insertUser(User u) throws Exception{
		
		String email = u.getEmail();
	    if (!isValidEmail(email)) {
	        throw new Exception("유효하지 않은 이메일 형식입니다.");
	    }		
		
	    String pwd = u.getPwd();
	    if (!isValidPassword(pwd)) {
	        throw new Exception("패스워드는 8자리 이상이어야 하며, 특수문자와 숫자를 포함해야 합니다.");
	    }
	    
		String salt = UUID.randomUUID().toString();
		byte[] originalHash = OpenCrypt.getSHA256(pwd, salt);
		String pwdHash = OpenCrypt.byteArrayToHex(originalHash);
		u.setPwd(pwdHash);
	    
		saltDao.insertSalt(new SaltInfo(email, salt));
		userDao.insertUser(u);
	}
	
	public void updateUser(User u) throws Exception{
		userDao.updateUser(u);
	}
	
	public void deleteUser(String email) throws Exception{
		userDao.deleteUser(email);
	}
	
    public void logout(String authorization) throws Exception {
        if (authorization == null) {
            System.out.println("⚠️ 로그아웃 요청 거부 - 토큰 없음");
            return;
        }

        loginDao.deleteToken(authorization);
        System.out.println("✅ 로그아웃 완료: " + authorization);
    }
	
	public boolean isNicknameAvailable(String nickname) {
		User existingUser = userDao.useByNickname(nickname);
		return existingUser == null; 
	}
	
	public void updateExpTime(String token, long newExpTime) throws Exception {
	    if (token == null) {
	        System.out.println("⚠️ 로그인 시간 갱신 요청 거부 - 토큰 없음");
	        return;
	    }

	    Login loginInfo = loginDao.getLoginInfo(token);
	    
	    if (loginInfo != null) {
	        long currentTime = System.currentTimeMillis();
	        
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
    
	private boolean isValidEmail(String email) {
	    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    return Pattern.matches(emailPattern, email);
	}
	
	private boolean isValidPassword(String password) {
	    String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
	    return Pattern.matches(passwordPattern, password);
	}
}