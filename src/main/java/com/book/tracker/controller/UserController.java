package com.book.tracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.book.tracker.dto.Login;
import com.book.tracker.dto.User;
import com.book.tracker.service.UserService;

@RestController
@CrossOrigin("http://127.0.0.1:5500/")
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("logout")
	public void logout(@RequestHeader String authorization) {
		
        if (authorization == null) {
            System.out.println("⚠️ 로그아웃 요청 거부 - 토큰 없음");
            return;
        }

        try {
            userService.logout(authorization);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    // ✅ 사용자의 토큰이 만료되었는지 확인
	@GetMapping("checkToken")
	public Map<String, String> checkToken(@RequestHeader String authorization) {
	    Map<String, String> responseMap = new HashMap<>();
	    
        if (authorization == null) {
            responseMap.put("expired", "true");
            return responseMap;
        }    
	    
	    try {
	        Login loginInfo = userService.getLoginInfo(authorization);

	        if (loginInfo != null) {
	            long currentTime = System.currentTimeMillis();
	            
	            // ✅ 만료 시간(exp)이 현재 시간보다 작다면 로그아웃 처리
	            if (loginInfo.getExp() < currentTime) {
	                userService.logout(authorization);
	                responseMap.put("expired", "true"); // 만료됨
	            } else {
	            	responseMap.put("expired", "false"); // 유효함
	            }
	        } else {
	        	responseMap.put("expired", "true"); // DB에서 찾을 수 없음 (로그아웃 상태)
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        responseMap.put("expired", "true");
	    }
	    return responseMap;
	}
	
    // ✅ 요청할 때마다 exp(만료 시간) 갱신
    @PostMapping("updateLoginTime")
    public void updateLoginTime(@RequestHeader String authorization) {
        if (authorization == null) {
            System.out.println("⚠️ 로그인 시간 갱신 요청 거부 - 토큰 없음");
            return;
        }
        
        try {
            long newExpTime = System.currentTimeMillis() + (1 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@PostMapping("tokenLogin")
	public Map<String,String> tokenLogin(@RequestBody User u) {
		
		Map<String,String> responseMap = new HashMap<>();
		
		try {
			Login loginInfo = userService.tokenLogin(u);
			
			if(loginInfo != null && loginInfo.getNickname() != null && loginInfo.getToken() != null) {
	            // ✅ 현재 시간 + 30분을 만료 시간(exp)으로 설정
	            long expTime = System.currentTimeMillis() + (1 * 60 * 1000);
                loginInfo.setExp(expTime);
                userService.updateExpTime(loginInfo.getToken(), expTime);
	            
                responseMap.put("msg", "ok");
				responseMap.put("nickname", loginInfo.getNickname());
				responseMap.put("Authorization", loginInfo.getToken());
			}else {
				responseMap.put("msg", "다시 로그인 해주세요");
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseMap.put("msg", "다시 로그인 해주세요");
		}
		return responseMap;
	}

	@PostMapping("insertUser")
	public Map<String,String> insertUser(@RequestBody User u) {
		System.out.println(u);
		
		Map<String,String> responseData = new HashMap<>();
		try {
			userService.insertUser(u);
			responseData.put("msg","ok");
		} catch (Exception e) {
			e.printStackTrace();
			responseData.put("msg",e.getMessage());
		}

		return responseData;
	}
	
	@PostMapping("updateUser")
	public String updateUser(@RequestBody User u) {
		System.out.println(u);
		try {
			userService.updateUser(u);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return "email과 pwd 확인해 주세요";
		}
	}
	
	@PostMapping("deleteUser")
	public String deleteUser(@RequestBody String email) {
		System.out.println(email);
		try {
			userService.deleteUser(email);
			return "ok";
		} catch (Exception e) {
			e.printStackTrace();
			return "email과 pwd 확인해 주세요";
		}
	}
	
	@GetMapping("checkNickname")
	public Map<String, Boolean> checkNickname(@RequestParam String nickname) {
	    System.out.println("닉네임 중복 확인 요청: " + nickname);
	    
	    boolean isAvailable = userService.isNicknameAvailable(nickname);
	    
	    Map<String, Boolean> response = new HashMap<>();
	    response.put("available", isAvailable);
	    
	    return response;
	}
}