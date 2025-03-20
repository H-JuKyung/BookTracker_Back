package com.book.tracker.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.book.tracker.dto.Goal;

@Mapper
public interface GoalDao {
    
    Goal getGoalByEmail(@Param("email") String email);

    void insertGoal(Goal goal);

    void deleteGoal(@Param("email") String email);

    void updateCurrentBooks(@Param("email") String email, @Param("currentBooks") int currentBooks);

    void updateGoalCompletion(@Param("email") String email, @Param("isCompleted") boolean isCompleted);

    void updateGoalTarget(@Param("email") String email, @Param("targetBooks") int targetBooks);
}
