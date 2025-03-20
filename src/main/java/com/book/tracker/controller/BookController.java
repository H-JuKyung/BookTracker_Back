package com.book.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.book.tracker.dto.Book;
import com.book.tracker.dto.Login;
import com.book.tracker.service.BookService;
import com.book.tracker.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("http://127.0.0.1:5500/")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Value("${ALADIN_API_KEY}")  
    private String aladinApiKey;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private UserService userService; 

    @GetMapping("/search")
    public ResponseEntity<String> searchBooks(@RequestParam String keyword) {
        // 알라딘 API 호출 URL 
        String apiUrl = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=" + aladinApiKey +
                "&Query=" + keyword +
                "&QueryType=Keyword" +
                "&MaxResults=50" +
                "&start=1&SearchTarget=Book&output=js&Version=20131101";

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveBook(@RequestHeader String authorization, @RequestBody Book book) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body("Unauthorized: 유효하지 않은 토큰");
            }

            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);

            System.out.println("저장 요청한 사용자 이메일: " + loginInfo.getEmail());

            book.setEmail(loginInfo.getEmail());

            Book existingBook = bookService.getBookByEmailAndTitle(book.getEmail(), book.getTitle());
            if (existingBook != null) {
                System.out.println("이미 저장된 책입니다: " + book.getTitle());
                return ResponseEntity.status(409).body("이미 담겨 있습니다.");
            }

            bookService.insertBook(book);
            System.out.println("책 저장 완료: " + book);

            return ResponseEntity.ok("책이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("책 저장 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/user-books")
    public ResponseEntity<List<Book>> getUserBooks(@RequestHeader String authorization, @RequestParam(required = false) String status) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).build();
            }

            List<Book> books;
            if (status != null) {
                books = bookService.getBooksByStatus(loginInfo.getEmail(), status);
            } else {
                books = bookService.getBooksByEmail(loginInfo.getEmail());
            }
            
             System.out.println("📚 반환되는 책 목록: " + books);

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @GetMapping("/list")
    public List<Book> getAllBooks() throws Exception {
        return bookService.getAllBooks();
    }

    @GetMapping("/{title}")
    public Book getTitleBook(@PathVariable String title) throws Exception {
        return bookService.getTitleBook(title);
    }

    @GetMapping("/reading-list")
    public ResponseEntity<List<Book>> getReadingList(@RequestHeader String authorization) {
        try {

            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null);
            }

            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
            

            return ResponseEntity.ok(bookService.getReadingList(loginInfo.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/want-to-read")
    public ResponseEntity<List<Book>> getWantToReadBooks(@RequestHeader String authorization) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null);
            }
            
            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
            
            return ResponseEntity.ok(bookService.getBooksByStatus(loginInfo.getEmail(), "읽고 싶어요"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }


    @GetMapping("/reading-now")
    public ResponseEntity<List<Book>> getReadingNowBooks(@RequestHeader String authorization) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null);
            }

            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
            
            return ResponseEntity.ok(bookService.getBooksByStatus(loginInfo.getEmail(), "읽고 있어요"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/reading-done")
    public ResponseEntity<List<Book>> getReadingDoneBooks(@RequestHeader String authorization) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null);
            }
            
            // 만료 시간 갱신 (사용자가 요청할 때마다 갱신)
            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
            
            return ResponseEntity.ok(bookService.getBooksByStatus(loginInfo.getEmail(), "다 읽었어요"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/update-status")
    public ResponseEntity<String> updateBookStatus(@RequestHeader String authorization, @RequestBody Book book) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body("Unauthorized: 유효하지 않은 토큰");
            }
            
            // ✅ 만료 시간 갱신 (사용자가 요청할 때마다 갱신)
            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);

            // 사용자의 책 상태 업데이트
            book.setEmail(loginInfo.getEmail());
            bookService.updateBookStatus(book);

            return ResponseEntity.ok("책 상태가 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("책 상태 업데이트 중 오류 발생: " + e.getMessage());
        }
    }
    
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteBook(
            @RequestHeader("Authorization") String token,
            @RequestParam int book_id) {

        System.out.println("📌 DELETE 요청 도착 - book_id: " + book_id);

        try {
            int result = bookService.deleteBook(token, book_id);
            return ResponseEntity.ok("삭제 성공! 삭제된 책 개수: " + result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("❌ 삭제 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("🚨 서버 오류 발생: " + e.getMessage());
        }
    }

}
