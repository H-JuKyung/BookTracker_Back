package com.book.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.book.tracker.dao.BookDao;
import com.book.tracker.dto.Book;

import java.util.List;

@Service
public class BookService {

    @Autowired
 BookDao bookDao; // DAO 연결

    //  책 추가 (읽고 싶어요 상태로 저장)
    public void insertBook(Book book) throws Exception {
        bookDao.insertBook(book);
    }

    //  모든 책 조회
    public List<Book> getAllBooks() throws Exception {
        return bookDao.getAllBooks();
    }

    // 특정 책 조회
    public Book getTitleBook (String title) throws Exception {
        return bookDao.getTitleBook(title);
    }

    // 책 상태 변경 (읽고 있어요 → 다 읽었어요 등)
//    public void updateBook(String title)throws Exception {
//    	System.out.println();
//        bookDao.updateBook(title);
//    }

    //  책 삭제
//    public void deleteBook(String title) throws Exception {
//        bookDao.deleteBook(title);
//    }

	public Book getBookById(int bookId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// 읽고 싶어요 책 목록 가져오는 메서드 
	public List<Book> getReadingList() throws Exception {
		return bookDao.getReadingList();
	}
}
