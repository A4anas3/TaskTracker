package com.corperate.TaskTracker.DTO;

import com.corperate.TaskTracker.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponse {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String mobileNo;
    private Role role;
    private String picure;
}
