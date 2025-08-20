package com.corperate.TaskTracker.Repository;

import com.corperate.TaskTracker.Model.Role;
import com.corperate.TaskTracker.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AdminRepo extends JpaRepository<User,Integer> {
    boolean existsByEmail(String email);

    boolean existsByMobileNo(String mobileNo);

    List<User> findByRole(Role role);
}
