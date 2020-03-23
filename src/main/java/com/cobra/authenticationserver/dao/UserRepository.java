package com.cobra.authenticationserver.dao;

import com.cobra.authenticationserver.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
