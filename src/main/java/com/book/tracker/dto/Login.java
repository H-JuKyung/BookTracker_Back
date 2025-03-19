package com.book.tracker.dto;

import java.util.Date;

public class Login {

    private String email, token, nickname;
    private Date loginTime;
    private long exp; 

    public Login() {
        super();
    }

    public Login(String email, String token, String nickname, Date loginTime, long exp) {
        super();
        this.email = email;
        this.token = token;
        this.nickname = nickname;
        this.loginTime = loginTime;
        this.exp = exp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    @Override
    public String toString() {
        return "Login [email=" + email + ", token=" + token + ", nickname=" + nickname + 
               ", loginTime=" + loginTime + ", exp=" + exp + "]";
    }
}
