package com.book.tracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Value;

import com.book.tracker.dto.Book;

@Mapper
public interface BookDao {

	public void insertBook(Book book) throws Exception;

	public List<Book> getAllBooks() throws Exception;

	public Book getTitleBook(String title) throws Exception;

	public List<Book> getReadingList() throws Exception;
	
	public List<Book> getBooksByEmail(String email) throws Exception;
	
    Integer getNextBookIdByEmail(String email) throws Exception;

    Book getBookByEmailAndTitle(@Param("email") String email, @Param("title") String title);

	public List<Book> getReadingListByEmail(String email) throws Exception;

	public List<Book> getBooksByStatus(String email, String status) throws Exception;
	
	void updateBookStatus(Book book);
	
	String getUserEmailByToken(@Param("token") String token);

	int deleteBook(@Param("email") String email, @Param("book_id") int book_id);

	int countBooksByEmail(@Param("email") String email);
    
}
