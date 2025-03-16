package com.book.tracker.dto;

public class Book {
	private int book_id;   
    private String title;
    private String author;
    private String publisher;
    private String cover;
    private String status;  

	public Book() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Book(int book_id, String title, String author, String publisher, String cover, String status
			) {
		super();
		this.book_id = book_id;
		this.title = title;
		this.author = author;
		this.publisher = publisher;
		this.cover = cover;
		this.status = status;
		
	}
	@Override
	public String toString() {
		return "Book [book_id=" + book_id + ", title=" + title + ", author=" + author + ", publisher=" + publisher
				+ ", cover=" + cover + ", status=" + status + ", statusDate="  + "]";
	}
	public int getBook_id() {
		return book_id;
	}
	public void setBook_id(int book_id) {
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