package com.corperate.TaskTracker.Repository;

import com.corperate.TaskTracker.Model.tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<tasks,Long> {
}
