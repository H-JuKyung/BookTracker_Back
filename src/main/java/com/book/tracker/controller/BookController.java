package com.book.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.book.tracker.dto.Book;
import com.book.tracker.dto.Login;
import com.book.tracker.service.BookService;
import com.book.tracker.service.UserService;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@CrossOrigin("http://127.0.0.1:5500/")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Value("${ALADIN_API_KEY}")  // secu.properties에서 불러옴
    private String aladinApiKey;
    
    @Autowired
    private BookService bookService;
    
    @Autowired
    private UserService userService; // authorization 토큰 포함 요청 확인하는거

    // ✅ 알라딘 API를 활용한 도서 검색 기능
    @GetMapping("/search")
    public ResponseEntity<String> searchBooks(@RequestParam String keyword) {
        // 알라딘 API 호출 URL 
        String apiUrl = "https://www.aladin.co.kr/ttb/api/ItemSearch.aspx" +
                "?ttbkey=" + aladinApiKey +
                "&Query=" + keyword +
                "&QueryType=Keyword" +
                "&MaxResults=50" +
                "&start=1&SearchTarget=Book&output=js&Version=20131101";

        // 외부 API 호출
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        // 프론트엔드로 결과 전달
        return ResponseEntity.ok(response);
    }

    // ✅ 검색한 책을 DB에 저장하는 기능 (읽고 싶어요)
    @PostMapping("/save")
    public ResponseEntity<String> saveBook(@RequestHeader String authorization, @RequestBody Book book) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body("Unauthorized: 유효하지 않은 토큰");
            }

            // ✅ 만료 시간 갱신 (사용자가 요청할 때마다 갱신)
            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);

            // ✅ 로그인한 이메일 확인
            System.out.println("📌 저장 요청한 사용자 이메일: " + loginInfo.getEmail());

            book.setEmail(loginInfo.getEmail());

            // ✅ 중복 검사
            Book existingBook = bookService.getBookByEmailAndTitle(book.getEmail(), book.getTitle());
            if (existingBook != null) {
                System.out.println("⚠️ 이미 저장된 책입니다: " + book.getTitle());
                return ResponseEntity.status(409).body("이미 담겨 있습니다.");
            }

            // ✅ 책 저장 실행
            bookService.insertBook(book);
            System.out.println("✅ 책 저장 완료: " + book);

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
            
            // ✅ 로그 추가 (반환되는 책 데이터 확인)
            System.out.println("📚 반환되는 책 목록: " + books);

            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
    
    // ✅ DB에 저장된 모든 책 조회
    @GetMapping("/list")
    public List<Book> getAllBooks() throws Exception {
        return bookService.getAllBooks();
    }

    // ✅ 특정 책 조회
    @GetMapping("/{title}")
    public Book getTitleBook(@PathVariable String title) throws Exception {
        return bookService.getTitleBook(title);
    }

    @GetMapping("/reading-list")
    public ResponseEntity<List<Book>> getReadingList(@RequestHeader String authorization) {
        try {
            // 로그인된 사용자 확인
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null);
            }
            
            // ✅ 만료 시간 갱신 (사용자가 요청할 때마다 갱신)
            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
            
            // 현재 로그인한 사용자의 `읽고 싶어요` 상태인 책 가져오기
            return ResponseEntity.ok(bookService.getReadingList(loginInfo.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ✅ "읽고 싶어요" 책 목록 가져오기
    @GetMapping("/want-to-read")
    public ResponseEntity<List<Book>> getWantToReadBooks(@RequestHeader String authorization) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null);
            }
            
            // ✅ 만료 시간 갱신 (사용자가 요청할 때마다 갱신)
            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
            
            return ResponseEntity.ok(bookService.getBooksByStatus(loginInfo.getEmail(), "읽고 싶어요"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ✅ "읽고 있어요" 책 목록 가져오기
    @GetMapping("/reading-now")
    public ResponseEntity<List<Book>> getReadingNowBooks(@RequestHeader String authorization) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null);
            }
            
            // ✅ 만료 시간 갱신 (사용자가 요청할 때마다 갱신)
            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
            
            return ResponseEntity.ok(bookService.getBooksByStatus(loginInfo.getEmail(), "읽고 있어요"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ✅ "다 읽었어요" 책 목록 가져오기
    @GetMapping("/reading-done")
    public ResponseEntity<List<Book>> getReadingDoneBooks(@RequestHeader String authorization) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body(null);
            }
            
            // ✅ 만료 시간 갱신 (사용자가 요청할 때마다 갱신)
            long newExpTime = System.currentTimeMillis() + (30 * 60 * 1000);
            userService.updateExpTime(authorization, newExpTime);
            
            return ResponseEntity.ok(bookService.getBooksByStatus(loginInfo.getEmail(), "다 읽었어요"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // ✅ 책 상태 변경 (읽고 있어요 / 다 읽었어요)
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
}
