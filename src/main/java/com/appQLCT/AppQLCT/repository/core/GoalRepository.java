package com.appQLCT.AppQLCT.repository.core;

import com.appQLCT.AppQLCT.entity.authentic.User;
import com.appQLCT.AppQLCT.entity.core.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUser(User user);
}
