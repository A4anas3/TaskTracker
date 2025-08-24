package com.corperate.TaskTracker.DTO.JwtDto;

import com.corperate.TaskTracker.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtDto {

    private Integer id;
    private  String email;
    private Role role;
}
