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
	
	public Login checkToken(String authorization) throws Exception {
		// TODO Auto-generated method stub
		return loginDao.checkToken(authorization);
	}
	
	public Login tokenLogin(User u) throws Exception {
		String email = u.getEmail();
		//email로 salt를 찾아옴
		SaltInfo saltInfo = saltDao.selectSalt(email);
		//pwd에 salt를 더하여 암호화
		String pwd = u.getPwd();
		byte [] pwdHash = OpenCrypt.getSHA256(pwd, saltInfo.getSalt());
		String pwdHashHex = OpenCrypt.byteArrayToHex(pwdHash);
		u.setPwd(pwdHashHex);
		u = userDao.login(u);		
		
		if(u != null) {
			String nickname = u.getNickname();
			if(nickname != null && !nickname.trim().equals("")) {
				//member table에서 email과 pwd가 확인된 상황 즉, login ok				
				
				//1. salt를 생성한다
				String salt = UUID.randomUUID().toString();
				System.out.println("salt: " + salt);
				//2. email을 hashing 한다
				byte[] originalHash = OpenCrypt.getSHA256(email, salt);
				//3. db에 저장하기 좋은 포맷으로 인코딩한다
				String myToken = OpenCrypt.byteArrayToHex(originalHash);
				System.out.println("myToken: " + myToken);
				
				//4. login table에 token 저장
				Login loginInfo = new Login(email, myToken, nickname, new Date());
				loginDao.insertToken(loginInfo);
				return loginInfo;
			}
		}
		
		return null;		 
	}
	
//	public User login(User u) throws Exception {
//	    return userDao.login(u);
//	}
	
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
	    //1. salt를 생성한다
		String salt = UUID.randomUUID().toString();
		System.out.println("salt: " + salt);
		//2. pwd를 hashing 한다
		byte[] originalHash = OpenCrypt.getSHA256(pwd, salt);
		//3. db에 저장하기 좋은 포맷으로 인코딩한다
		String pwdHash = OpenCrypt.byteArrayToHex(originalHash);
		System.out.println("pwdHash: " + pwdHash);
	    
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
		loginDao.deleteToken(authorization);
	}
	
	public boolean isNicknameAvailable(String nickname) {
		User existingUser = userDao.useByNickname(nickname);
		return existingUser == null; // 닉네임이 존재하지 않으면 true (사용 가능)
	}
	
	// 이메일 유효성 검사 메서드
	private boolean isValidEmail(String email) {
	    // 이메일 패턴
	    String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
	    return Pattern.matches(emailPattern, email);
	}
	
	// 패스워드 유효성 검사 메서드
	private boolean isValidPassword(String password) {
	    // 패스워드 패턴: 8자리 이상, 숫자 포함, 특수문자 포함
	    String passwordPattern = "^(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}$";
	    return Pattern.matches(passwordPattern, password);
	}
}
