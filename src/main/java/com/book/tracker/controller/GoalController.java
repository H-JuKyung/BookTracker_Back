package com.book.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.book.tracker.service.GoalService;
import com.book.tracker.service.UserService;
import com.book.tracker.dto.Goal;
import com.book.tracker.dto.Login;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("http://127.0.0.1:5500/")
@RestController
@RequestMapping("/api/goal")
public class GoalController {

    @Autowired
    private GoalService goalService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getGoal(@RequestHeader String authorization) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body("⛔ Unauthorized: 유효하지 않은 토큰입니다.");
            }

            userService.updateExpTime(authorization, System.currentTimeMillis() + (30 * 60 * 1000));

            Goal goal = goalService.getGoal(loginInfo.getEmail());
            if (goal == null) {
                return ResponseEntity.ok("🚫 설정된 목표가 없습니다.");
            }

            return ResponseEntity.ok(goal);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("⚠️ 목표 조회 중 오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/set")
    public ResponseEntity<?> setGoal(@RequestHeader String authorization, @RequestParam int targetBooks) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body("⛔ Unauthorized: 유효하지 않은 토큰입니다.");
            }

            userService.updateExpTime(authorization, System.currentTimeMillis() + (30 * 60 * 1000));

            goalService.setGoal(loginInfo.getEmail(), targetBooks);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "✅ 목표가 저장되었습니다.");
            response.put("targetBooks", targetBooks);
            response.put("currentBooks", 0);  

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("⚠️ 목표 저장 중 오류 발생: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGoal(@RequestHeader String authorization) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body("⛔ Unauthorized: 유효하지 않은 토큰입니다.");
            }

            userService.updateExpTime(authorization, System.currentTimeMillis() + (30 * 60 * 1000));

            goalService.deleteGoal(loginInfo.getEmail());

            Map<String, String> response = new HashMap<>();
            response.put("message", "🗑 목표가 삭제되었습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("⚠️ 목표 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    @PutMapping("/increase")
    public ResponseEntity<?> increaseCurrentBooks(@RequestHeader String authorization, @RequestParam int currentBooks) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body("⛔ Unauthorized: 유효하지 않은 토큰입니다.");
            }

            Goal goal = goalService.getGoal(loginInfo.getEmail());
            if (goal == null) {
                return ResponseEntity.status(404).body("🚫 설정된 목표가 없습니다.");
            }

            goalService.updateCurrentBooks(loginInfo.getEmail(), currentBooks);

            if (currentBooks >= goal.getTargetBooks()) {
                goalService.updateGoalCompletion(loginInfo.getEmail(), true);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("message", "📚 현재 읽은 책 수가 업데이트되었습니다.");
            response.put("currentBooks", currentBooks);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("⚠️ 현재 읽은 책 수 업데이트 중 오류 발생: " + e.getMessage());
        }
    }

    @PostMapping("/updateTarget")
    public ResponseEntity<?> updateGoalTarget(
        @RequestHeader String authorization,
        @RequestParam String email,
        @RequestParam int targetBooks
    ) {
        try {
            Login loginInfo = userService.getLoginInfo(authorization);
            if (loginInfo == null) {
                return ResponseEntity.status(401).body("⛔ Unauthorized: 유효하지 않은 토큰입니다.");
            }

            Goal goal = goalService.getGoal(email);
            if (goal == null) {
                return ResponseEntity.status(404).body("🚫 설정된 목표가 없습니다.");
            }

            goalService.updateGoalTarget(email, targetBooks);

            return ResponseEntity.ok("🎯 목표 변경 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("⚠️ 목표 변경 중 오류 발생: " + e.getMessage());
        }
    }
}
