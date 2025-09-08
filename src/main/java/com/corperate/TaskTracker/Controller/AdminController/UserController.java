package com.corperate.TaskTracker.Controller.AdminController;


import com.corperate.TaskTracker.DTO.AdminUserRequest;
import com.corperate.TaskTracker.DTO.AdminUserResponse;
import com.corperate.TaskTracker.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor

public class UserController {
    @Autowired
    private  final UserService userService;




    @PostMapping(value="/create-user",consumes = "multipart/form-data")
    public ResponseEntity<String> createUser(@RequestPart("request") AdminUserRequest request,@RequestPart(name = "image",required = false) MultipartFile image){
        String output= null;
        try {
            output = userService.createUser(request,image);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage()
            );
        }
        return  ResponseEntity.ok(output);
     }


     @PutMapping(value = "/update-user/{id}",consumes = "multipart/form-data")

    public ResponseEntity<String> updateUser(@PathVariable Integer id,@RequestPart("request") AdminUserRequest request, @RequestPart(name = "image",required = false) MultipartFile image){
       String s="";
         try {
             s= userService.updateUser(id,request,image);
         } catch (IOException e) {
             throw new RuntimeException("Error while Updating" + e.getMessage());
         }
         return new ResponseEntity<>(s,HttpStatus.CREATED);
     }

     @GetMapping("/getAllEmployee")
    public ResponseEntity<List<AdminUserResponse>> findAll(){
        List<AdminUserResponse> e = userService.findAll();
        return ResponseEntity.ok(e);
     }
    @GetMapping("/getAllEmployeebyRole")
    public ResponseEntity<List<AdminUserResponse>> findAllbyRole(@RequestParam String role){
        List<AdminUserResponse> e = userService.findAllByRole(role);
        return ResponseEntity.ok(e);
    }


    @PostMapping("/deleteUser/{id}")

    public ResponseEntity<String> delete(@PathVariable Integer id){
        return ResponseEntity.ok(userService.delete(id));
    }

    @PutMapping("updateRole/{id}")
    public ResponseEntity<String> updaterole(@PathVariable Integer id,@RequestParam String role ){
        return ResponseEntity.ok(userService.updateRole(id ,role));
    }





}
