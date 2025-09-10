package com.corperate.TaskTracker.Controller;

import com.corperate.TaskTracker.DTO.LoginAuthDTO.LoginDto;
import com.corperate.TaskTracker.DTO.LoginAuthDTO.LoginResponseDto;
import com.corperate.TaskTracker.Service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")
public class LoginController {
    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        String token = authService.login(loginDto);

        Cookie jwtCookie = new Cookie("jwt", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Set to false for localhost; true in production
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3 * 60); // 3 minutes expiry

        response.addCookie(jwtCookie);

        return new LoginResponseDto("Login successful", token);
    }



}
