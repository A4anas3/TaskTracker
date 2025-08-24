package com.corperate.TaskTracker.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(name = "user_seq" ,sequenceName="user_seq",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_seq")
    private int id;
    private String firstname;
    private String lastname;
    @Column(unique = true)
    private String email;
    private String mobileNo;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Lob

    private byte[] profilePicture;
@Column(nullable = false)
    private String password;
    private boolean enabled = true;         // default true
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;
    private boolean credentialsNonExpired = true;




}
