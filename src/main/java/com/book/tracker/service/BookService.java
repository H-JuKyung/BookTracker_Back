package com.book.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.book.tracker.dao.BookDao;
import com.book.tracker.dao.LoginDao;
import com.book.tracker.dto.Book;

import java.util.List;

@Service
public class BookService {
	 @Autowired
	LoginDao loginDao;

    @Autowired
    BookDao bookDao; 

    @Transactional
    public void insertBook(Book book) throws Exception {
       
        Integer nextBookId = bookDao.getNextBookIdByEmail(book.getEmail());
        book.setBook_id(nextBookId);
        
      
        bookDao.insertBook(book);
    }
 
    public List<Book> getAllBooks() throws Exception {
        return bookDao.getAllBooks();
    }

 
    public Book getTitleBook (String title) throws Exception {
        return bookDao.getTitleBook(title);
    }

	public Book getBookById(int book_id) {
		return null;
	}
	

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
	 

	 @Transactional
	    public int deleteBook(String token, int book_id) throws Exception {
	        System.out.println("BookService - 삭제 요청 실행: book_id=" + book_id);

	     
	        String email = loginDao.getUserEmailByToken(token);
	        if (email == null || email.isEmpty()) {
	            System.out.println("삭제 실패: 해당 토큰에 연결된 이메일 없음!");
	            throw new IllegalArgumentException("유효하지 않은 사용자 토큰입니다.");
	        }

	        System.out.println("삭제할 사용자 이메일: " + email);

	        // 📌 책 삭제 실행
	        int rowsAffected = bookDao.deleteBook(email, book_id);
	        if (rowsAffected == 0) {
	            System.out.println("삭제 실패: 해당 book_id를 가진 책이 없음!");
	            throw new IllegalArgumentException("삭제할 책을 찾을 수 없습니다.");
	        }

	        System.out.println("삭제 완료, 영향을 받은 행 수: " + rowsAffected);

	        return rowsAffected; 
	    }
}
