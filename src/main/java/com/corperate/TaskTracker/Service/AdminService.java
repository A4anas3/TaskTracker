package com.corperate.TaskTracker.Service;

import com.corperate.TaskTracker.DTO.AdminUserRequest;
import com.corperate.TaskTracker.DTO.AdminUserResponse;
import com.corperate.TaskTracker.Model.Role;
import com.corperate.TaskTracker.Model.User;
import com.corperate.TaskTracker.Repository.AdminRepo;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AdminService {
    @Autowired
    private final AdminRepo repo;
    public  String createUser(AdminUserRequest request,MultipartFile image) throws IOException {
        if(repo.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email Already exists");

        } else if (repo.existsByMobileNo(request.getMobileNo())) {
            throw new RuntimeException("mobile no already exits");

        }

        User user =new User();
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new RuntimeException("Email cannot be null or empty");
        }
        user.setEmail(request.getEmail());
        user.setMobileNo(request.getMobileNo());

        if (request.getRole() == null || request.getRole().isBlank()) {
            throw new RuntimeException("Role cannot be null or empty");
        }
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        user.setProfilePicture(image.getBytes());

        repo.save(user);
        return "User created";






    }

    public String updateUser(Integer id,AdminUserRequest request, MultipartFile image) throws IOException{
        User user=repo.findById(id)
                .orElseThrow(()->new RuntimeException("user not found with id"+id));

        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new BadRequestException("Email cannot be null or empty");
        }
        user.setEmail(request.getEmail());
        user.setMobileNo(request.getMobileNo());

        if (request.getRole() == null || request.getRole().isBlank()) {
            throw new BadRequestException("Role cannot be null or empty");
        }
        user.setRole(Role.valueOf(request.getRole().toUpperCase()));
        user.setProfilePicture(image.getBytes());

        repo.save(user);
        return  "User credential Updated";
    }

    public List<AdminUserResponse> findAll() {

        List<User> ei = repo.findAll();

        return ei.stream()
                .map(e-> new AdminUserResponse(
                        e.getId(),
                        e.getFirstname(),
                        e.getLastname(),
                        e.getMobileNo(),
                        e.getEmail(),
                        e.getRole(),
                        e.getProfilePicture()!=null?Base64.getEncoder().encodeToString(e.getProfilePicture()):null
                )).toList();
    }

    public List<AdminUserResponse> findAllByRole(String role){

    List<User> ei = repo.findByRole(Role.valueOf(role.toUpperCase()));

        return ei.stream()
                .map(e-> new AdminUserResponse(
            e.getId(),
                        e.getFirstname(),
                                e.getLastname(),
                                e.getMobileNo(),
                                e.getEmail(),
                                e.getRole(),
                                e.getProfilePicture()!=null? Base64.getEncoder().encodeToString(e.getProfilePicture()) :null
                                )).toList();
}

    public String delete(Integer id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("User with ID " + id + " not found");
        }
        repo.deleteById(id);
        return "delete successfully";
    }

    public String updateRole(Integer id,String role) {

        User user = repo.findById(id).orElseThrow(()->new RuntimeException("User with ID " + id + " not found"));

        user.setRole(Role.valueOf(role.toUpperCase()));
        repo.save(user);

        return "Successfully updated";
    }
}
