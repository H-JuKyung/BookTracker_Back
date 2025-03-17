package com.book.tracker.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

//public void updateBook(String title) throws Exception;
//	public void deleteBook(String title) throws Exception;
   
	// 읽고 싶어요 책 목록 가져오는 메서드 
	public List<Book> getReadingList() throws Exception;


//public List<Book> getBooksByStatus(String status) throws Exception;


}
