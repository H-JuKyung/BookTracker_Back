package com.book.tracker.dto;

public class Book {
	private String email;
	private Integer book_id;   
    private String title;
    private String author;
    private String publisher;
    private String cover;
    private String status;
	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Book(String email, Integer book_id, String title, String author, String publisher, String cover,
			String status) {
		super();
		this.email = email;
		this.book_id = book_id;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.cover = cover;
		this.status = status;
	}
	@Override
	public String toString() {
		return "Book [email=" + email + ", book_id=" + book_id + ", title=" + title + ", author=" + author
				+ ", publisher=" + publisher + ", cover=" + cover + ", status=" + status + "]";
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getBook_id() {
		return book_id;
	}
	public void setBook_id(Integer book_id) {
		this.book_id = book_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getCover() {
		return cover;
	}
	public void setCover(String cover) {
		this.cover = cover;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
	
	
}