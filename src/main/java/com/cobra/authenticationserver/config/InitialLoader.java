package com.cobra.authenticationserver.config;

import com.cobra.authenticationserver.dao.RoleRepository;
import com.cobra.authenticationserver.dao.UserRepository;
import com.cobra.authenticationserver.dto.Role;
import com.cobra.authenticationserver.dto.RoleName;
import com.cobra.authenticationserver.dto.User;
import com.cobra.authenticationserver.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class InitialLoader {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRegistrationService registrationService;

    @PostConstruct
    private void defaultData() {
        createRoles();
        if (userRepository.count() <= 0) {
            registrationService.register(new User("test","test","test@gmail.com","tester","123456"), RoleName.SUPER_ADMIN.name());
        }
    }

    private void createRoles() {
        if (roleRepository.count() <= 0) {
            List<Role> roles = new ArrayList<>();

            for (RoleName roleName : RoleName.values()) {
                Role role = new Role();
                role.setName(roleName.name());
                roles.add(role);
            }

            roleRepository.saveAll(roles);
        }
    }
}
