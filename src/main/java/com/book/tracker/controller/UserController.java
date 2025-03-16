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
		System.out.println(authorization);
		try {
			userService.logout(authorization);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PostMapping("tokenLogin")
	public Map<String,String> tokenLogin(@RequestBody User u) {
		System.out.println(u);
		
		Map<String,String> responseMap = new HashMap<>();
		
		try {
			Login loginInfo = userService.tokenLogin(u);
			
			if(loginInfo != null && loginInfo.getNickname() != null && loginInfo.getToken() != null) {
                responseMap.put("msg", "ok");
				responseMap.put("nickname", loginInfo.getNickname());
				responseMap.put("Authorization", loginInfo.getToken());
			}else {
				responseMap.put("msg", "다시 로그인 해주세요");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			responseMap.put("msg", "다시 로그인 해주세요");
		}
		return responseMap;
	}
	
//	@PostMapping("login")
//	public Map<String,String> login(@RequestBody User u) {
//		System.out.println(u);
//		
//		Map<String,String> responseMap = new HashMap<>();
//		
//		try {
//			u = userService.login(u);
//			String nickname = u.getNickname();
//			if(u != null && nickname != null && !nickname.trim().equals("")) {
//				responseMap.put("nickname", nickname);
//			}else {
//				responseMap.put("msg", "다시 로그인 해주세요");
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			responseMap.put("msg", "다시 로그인 해주세요");
//		}
//		return responseMap;
//	}
	
	@PostMapping("insertUser")
	public Map<String,String> insertUser(@RequestBody User u) {
		System.out.println(u);
		
		Map<String,String> responseData = new HashMap<>();
		try {
			userService.insertUser(u);
			responseData.put("msg","ok");
		} catch (Exception e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
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
