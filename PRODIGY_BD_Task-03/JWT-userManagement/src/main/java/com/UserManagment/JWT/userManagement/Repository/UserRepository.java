package com.UserManagment.JWT.userManagement.Repository;


import com.UserManagment.JWT.userManagement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
