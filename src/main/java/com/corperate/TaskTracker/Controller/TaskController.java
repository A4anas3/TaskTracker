package com.corperate.TaskTracker.Controller;

import com.corperate.TaskTracker.DTO.AdminManagerHistoryDto.AdminHistorySave;
import com.corperate.TaskTracker.DTO.AdminTaskManagement.TaskDTOGet;
import com.corperate.TaskTracker.DTO.AdminTaskManagement.TaskDtoPost;
import com.corperate.TaskTracker.Service.TaskService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/TaskManagement")
public class TaskController {
    @Autowired
    private final TaskService taskService;
    //task management

    @PostMapping("/tasks")
    public ResponseEntity<String> createTask(@RequestBody TaskDtoPost dto ){
        String s= taskService.createTask(dto);

        return ResponseEntity.ok(s);

    }

    @PutMapping("updateTask/{id}")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody TaskDtoPost dto  ){
        String s= taskService.updateTask(id,dto);

        return ResponseEntity.ok(s);

    }

    @PostMapping("deleteTask/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id){
        String s= null;
        try {
            s = taskService.deleteTask(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(s);
    }

    @GetMapping("GetAllTask")

    public ResponseEntity<List<TaskDTOGet>> GetAllTask(){
        List<TaskDTOGet> t= null;
        try {
            t = taskService.GetAllTask();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(t);
    }
    @GetMapping("/GetTaskById/{id}")
    public ResponseEntity<TaskDTOGet> GatTaskById(@PathVariable Long id){
        TaskDTOGet t= null;
        try {
            t = taskService.GetTaskById(id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(t);
    }


    @GetMapping("/history")
    public ResponseEntity<List<AdminHistorySave>> History(){
        List<AdminHistorySave> s= taskService.History();
        return new ResponseEntity<>(s, HttpStatus.OK);
    }


    @GetMapping("TaskHistoryById/{id}")

    public ResponseEntity<List<AdminHistorySave>> TaskHistoryById(@PathVariable Long id){
        List<AdminHistorySave> s= taskService.TaskHistoryById(id);
        return ResponseEntity.ok(s);
    }

}
