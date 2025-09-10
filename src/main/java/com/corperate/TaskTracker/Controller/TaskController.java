package com.corperate.TaskTracker.Controller;

import com.corperate.TaskTracker.DTO.AdminManagerHistoryDto.AdminHistorySave;
import com.corperate.TaskTracker.DTO.AdminTaskManagement.TaskDTOGet;
import com.corperate.TaskTracker.DTO.AdminTaskManagement.TaskDtoPost;
import com.corperate.TaskTracker.Model.Principal.UserDetailPrincipal;
import com.corperate.TaskTracker.Model.User;
import com.corperate.TaskTracker.Service.TaskService;
import com.corperate.TaskTracker.Service.UserDetailSecurity.MyUserDetailsService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
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

    @DeleteMapping("deleteTask/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id,@AuthenticationPrincipal UserDetailPrincipal principal){
        String s= null;
        try {
            s = taskService.deleteTask(id,principal);
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
    @GetMapping("/myTaskAssigned")
    public ResponseEntity<List<TaskDTOGet>> myTaskassignedToMe(@AuthenticationPrincipal UserDetailPrincipal userDetails){
        int u= userDetails.getUser().getId();
        List<TaskDTOGet> s=taskService.getMyTask(u);
        if (s.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(s);
    }
    @GetMapping("/AssignedTaskByMe")
    public ResponseEntity<List<TaskDTOGet>> AssignedByMe(@AuthenticationPrincipal UserDetailPrincipal userDetailPrincipal){
        int u= userDetailPrincipal.getUser().getId();
        List<TaskDTOGet> s=taskService.AssignedByMe(u);
        if (s.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }

        return ResponseEntity.ok(s);
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

    @PatchMapping("UpdateStatus/{taskId}/{Status}")
        public ResponseEntity<TaskDTOGet> updateStatus(@PathVariable long taskId,@PathVariable String Status,@AuthenticationPrincipal UserDetailPrincipal userDetailPrincipal ){
        int id= userDetailPrincipal.getUser().getId();
        TaskDTOGet task = taskService.UpdateStatus(taskId,Status,id);


        if (task == null) {
            // If task not found or not assigned to him
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);

    }

}
