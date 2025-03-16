package com.book.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.book.tracker.dto.Book;
import com.book.tracker.service.BookService;

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

    // 알라딘 API를 활용한 도서 검색 기능
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

    // 2) 검색한 책을 DB에 저장하는 기능 (읽고 싶어요)
    @PostMapping("/save")
    public ResponseEntity<String> saveBook(@RequestBody Book book) {
        try {
            bookService.insertBook(book);
            return ResponseEntity.ok("책이 성공적으로 저장되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("책 저장 중 오류 발생: " + e.getMessage());
        }
    }

    // 3) DB에 저장된 모든 책 조회
    @GetMapping("/list")
    public List<Book> getAllBooks() throws Exception {
        return bookService.getAllBooks();
    }

    // 4) 특정 책 조회
    @GetMapping("/{title}")
    public Book getTitleBook(@PathVariable String title) throws Exception {
        return bookService.getTitleBook(title);
    }

    // 5) 책 상태 변경 (읽고 있어요, 다 읽었어요 등)
//    @PutMapping("/{title}/status")
//    public ResponseEntity<String> updateBookStatus(@PathVariable String title, @RequestBody Map<String, String> request) {
//        try {
//            String status = request.get("status");
//            bookService.updateBook(title); // title을 기준으로 상태 업데이트
//            return ResponseEntity.ok("책 상태가 업데이트되었습니다.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("책 상태 업데이트 중 오류 발생: " + e.getMessage());
//        }
//    }

    // 6) 책 삭제
//    @DeleteMapping("/{title}")
//    public ResponseEntity<String> deleteBook(@PathVariable String title) {
//        try {
//            bookService.deleteBook(title);
//            return ResponseEntity.ok("책이 삭제되었습니다.");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("책 삭제 중 오류 발생: " + e.getMessage());
//        }
//    }
    
 // 읽고 싶어요 책 목록 가져오는 메서드 
    @GetMapping("/reading-list")
    public List<Book> getReadingList() throws Exception {
        return bookService.getReadingList();
    }
    
}
