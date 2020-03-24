package com.cobra.authenticationserver.service;

import com.cobra.authenticationserver.dao.RoleRepository;
import com.cobra.authenticationserver.dao.UserRepository;
import com.cobra.authenticationserver.dto.Role;
import com.cobra.authenticationserver.dto.RoleName;
import com.cobra.authenticationserver.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserRegistrationService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public User register(User user, String authority) {
        user.setPassword(encryptPassword(user.getPassword()));
        List<String> roleNames = getRoles(authority);
        Set<Role> roles = new HashSet<>();

        if (roleNames.size() != 0) {
            for (String roleName : roleNames) {
                Role role = roleRepository.findByName(roleName);
                roles.add(role);
            }
        } else {
            roles.add(roleRepository.findByName("USER"));
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User me(Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }

    private List<String> getRoles(String roleName) {
        List<String> roles = new ArrayList<>();

        switch(roleName) {
            case "SUPER_ADMIN":
                for (RoleName role : RoleName.values()) {
                    roles.add(role.name());
                }
                break;
            case "ADMIN":
                roles.add(RoleName.ADMIN.name());
                roles.add(RoleName.USER.name());
                break;
            case "USER":
                roles.add(RoleName.USER.name());
                break;
            default:
                break;
        }

        return roles;
    }
}
