package com.book.tracker.dto;

import java.util.Date;

public class User {
	
	private String email, pwd, nickname;
	private Date registDate;
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Date getRegistDate() {
		return registDate;
	}
	public void setRegistDate(Date registDate) {
		this.registDate = registDate;
	}

	public User(String email, String pwd, String nickname, Date registDate) {
		super();
		this.email = email;
		this.pwd = pwd;
		this.nickname = nickname;
		this.registDate = registDate;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", pwd=" + pwd + ", nickname=" + nickname + ", registDate=" + registDate + "]";
	}
}
