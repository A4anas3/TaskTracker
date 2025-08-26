package com.corperate.TaskTracker.Repository;

import com.corperate.TaskTracker.Model.tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepo extends JpaRepository<tasks,Long> {
    List<tasks> findByAssignedTo_Id(Integer a);

    List<tasks> findByCreatedBy_Id(Integer a);
}
