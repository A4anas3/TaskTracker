package com.corperate.TaskTracker.Controller;

import com.corperate.TaskTracker.DTO.LoginAuthDTO.LoginDto;
//import com.corperate.TaskTracker.Service.JwtService.JwtService;

import com.corperate.TaskTracker.Service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class LoginController {
    private final AuthService authService;
    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }





}
