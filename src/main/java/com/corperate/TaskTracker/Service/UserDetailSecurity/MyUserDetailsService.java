package com.corperate.TaskTracker.Service.UserDetailSecurity;

import com.corperate.TaskTracker.Model.Principal.UserDetailPrincipal;
import com.corperate.TaskTracker.Model.User;
import com.corperate.TaskTracker.Repository.AdminRepoOfALL.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user= userRepo.findByEmail(email);
        if(user==null){
            System.out.println("User 404");
            throw new UsernameNotFoundException("User 404");
        }
        return new UserDetailPrincipal(user);
    }
}
