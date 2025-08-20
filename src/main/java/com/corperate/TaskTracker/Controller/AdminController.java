package com.corperate.TaskTracker.Controller;

import com.corperate.TaskTracker.DTO.AdminUserRequest;
import com.corperate.TaskTracker.DTO.AdminUserResponse;
import com.corperate.TaskTracker.Service.AdminService;
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
public class AdminController {
    @Autowired
    private  final AdminService adminService;



    @PostMapping(value="/create-user",consumes = "multipart/form-data")
//@PostMapping(value="/create-user")
    public ResponseEntity<String> createUser(@RequestPart("request") AdminUserRequest request,@RequestPart(name = "image",required = false) MultipartFile image){
        String output= null;
        try {
            output = adminService.createUser(request,image);
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
             s= adminService.updateUser(id,request,image);
         } catch (IOException e) {
             throw new RuntimeException("Error while Updating" + e.getMessage());
         }
         return new ResponseEntity<>(s,HttpStatus.CREATED);
     }

     @GetMapping("/getAllEmployee")
    public ResponseEntity<List<AdminUserResponse>> findAll(){
        List<AdminUserResponse> e = adminService.findAll();
        return ResponseEntity.ok(e);
     }
    @GetMapping("/getAllEmployeebyRole")
    public ResponseEntity<List<AdminUserResponse>> findAllbyRole(@RequestParam String role){
        List<AdminUserResponse> e = adminService.findAllByRole(role);
        return ResponseEntity.ok(e);
    }


    @PostMapping("/delete/{id}")

    public ResponseEntity<String> delete(@PathVariable Integer id){
        return ResponseEntity.ok(adminService.delete(id));
    }

    @PutMapping("updateRole/{id}")
    public ResponseEntity<String> updaterole(@PathVariable Integer id,@RequestParam String role ){
        return ResponseEntity.ok(adminService.updateRole(id ,role));
    }

}
