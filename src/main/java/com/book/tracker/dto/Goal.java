package com.book.tracker.dto;

import java.util.Date;

public class Goal {
	
    private String email; 
    private int targetBooks; 
    private int currentBooks;
    private boolean isCompleted; 
    private Date createdAt; 

    public Goal() {
        super();
    }

    public Goal(String email, int targetBooks, int currentBooks, boolean isCompleted, Date createdAt) {
        this.email = email;
        this.targetBooks = targetBooks;
        this.currentBooks = currentBooks;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTargetBooks() {
        return targetBooks;
    }

    public void setTargetBooks(int targetBooks) {
        this.targetBooks = targetBooks;
    }

    public int getCurrentBooks() {
        return currentBooks;
    }

    public void setCurrentBooks(int currentBooks) {
        this.currentBooks = currentBooks;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Goal [email=" + email + ", targetBooks=" + targetBooks + ", currentBooks=" + currentBooks 
                + ", isCompleted=" + isCompleted + ", createdAt=" + createdAt + "]";
    }
}
