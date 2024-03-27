package com.nagarro.java_mini_assignment_2.Service;

import com.nagarro.java_mini_assignment_2.Entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<UserDetails, String> {
}
