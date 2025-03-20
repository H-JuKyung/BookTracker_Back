package com.book.tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.book.tracker.dao.GoalDao;
import com.book.tracker.dto.Goal;

import java.util.Date;

@Service
public class GoalService {

    @Autowired
    private GoalDao goalDao;

    public Goal getGoal(String email) {
        return goalDao.getGoalByEmail(email);
    }

    public void setGoal(String email, int targetBooks) {
        Goal existingGoal = goalDao.getGoalByEmail(email);
        
        if (existingGoal == null) {
            Goal goal = new Goal(email, targetBooks, 0, false, new Date());
            goalDao.insertGoal(goal);
        } else {
            goalDao.updateGoalTarget(email, targetBooks);
            goalDao.updateCurrentBooks(email, 0);
            goalDao.updateGoalCompletion(email, false); 
        }
    }

    public void deleteGoal(String email) {
        goalDao.deleteGoal(email);
    }

    public void updateCurrentBooks(String email, int currentBooks) {
        goalDao.updateCurrentBooks(email, currentBooks);

        Goal goal = goalDao.getGoalByEmail(email);
        if (goal != null && goal.getCurrentBooks() >= goal.getTargetBooks()) {
            goalDao.updateGoalCompletion(email, true);  
        }
    }
    
    public void updateGoalTarget(String email, int targetBooks) {
        goalDao.updateGoalTarget(email, targetBooks);
    }
    
    public void updateGoalCompletion(String email, boolean isCompleted) {
        goalDao.updateGoalCompletion(email, isCompleted);
    }
}
