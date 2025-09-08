package com.corperate.TaskTracker.Service;

import com.corperate.TaskTracker.DTO.JwtDto.JwtDto;
import com.corperate.TaskTracker.DTO.LoginAuthDTO.LoginDto;
import com.corperate.TaskTracker.Model.Principal.UserDetailPrincipal;
import com.corperate.TaskTracker.Model.User;
import com.corperate.TaskTracker.Repository.AdminRepoOfALL.UserRepo;
import com.corperate.TaskTracker.Service.JwtService.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepo userRepo;



    public String login(LoginDto loginDto){
        Authentication authentication=authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));

        if (authentication.isAuthenticated()) {
            UserDetailPrincipal principal = (UserDetailPrincipal) authentication.getPrincipal();
            User authenticatedUser=principal.getUser();
            JwtDto jwtDto = new JwtDto();
            jwtDto.setId(authenticatedUser.getId());
            jwtDto.setRole(authenticatedUser.getRole());
            jwtDto.setEmail(authenticatedUser.getEmail());

            return jwtService.generateToken(jwtDto);



        } else {
            throw new RuntimeException("Invalid credentials");
        }

    }
}
