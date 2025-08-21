package com.corperate.TaskTracker.Repository.HistryTrack;

import com.corperate.TaskTracker.Model.Taskhistory;
import org.springframework.data.jpa.repository.JpaRepository;

//import java.lang.ScopedValue;
import java.util.List;

public interface TaskhistoryRepo extends JpaRepository<Taskhistory,Long> {
    List<Taskhistory> findByTaskId(Long taskId);
}
