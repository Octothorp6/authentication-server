package com.cobra.authenticationserver.dao;

import com.cobra.authenticationserver.dto.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
