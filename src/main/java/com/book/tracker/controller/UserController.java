package com.book.tracker.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.book.tracker.dto.User;
import com.book.tracker.service.UserService;

@RestController
@CrossOrigin("http://127.0.0.1:5501/")
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("insertUser")
	public Map<String, String> insertUser(@RequestBody  User u) {
		Map<String,String> responseData=new HashMap();
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
}
