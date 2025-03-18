package com.book.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.tracker.dao.BookDao;
import com.book.tracker.dto.Book;

import java.util.List;

@Service
public class BookService {

    @Autowired
    BookDao bookDao; // DAO 연결

    //  책 추가 (읽고 싶어요 상태로 저장)
    @Transactional
    public void insertBook(Book book) throws Exception {
        // Java에서 book_id를 직접 계산
        Integer nextBookId = bookDao.getNextBookIdByEmail(book.getEmail());
        book.setBook_id(nextBookId);
        
        // book_id를 설정한 후 DB에 저장
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

	public Book getBookById(int book_id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// 읽고 싶어요 책 목록 가져오는 메서드 
	public List<Book> getReadingList() throws Exception {
		return bookDao.getReadingList();
	}

	 public List<Book> getBooksByEmail(String email) throws Exception {
	        return bookDao.getBooksByEmail(email);
	    }
	 
	 public Book getBookByEmailAndTitle(String email, String title) {
		    return bookDao.getBookByEmailAndTitle(email, title);
		}

	 public List<Book> getReadingList(String email) throws Exception {
		    return bookDao.getReadingListByEmail(email);
		}

	 public List<Book> getBooksByStatus(String email, String status) throws Exception {
		    return bookDao.getBooksByStatus(email, status);
		}
	 public void updateBookStatus(Book book) {
	        bookDao.updateBookStatus(book);
	    }


}
