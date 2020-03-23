package com.cobra.authenticationserver.service;

import com.cobra.authenticationserver.dao.RoleRepository;
import com.cobra.authenticationserver.dao.UserRepository;
import com.cobra.authenticationserver.dto.Role;
import com.cobra.authenticationserver.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
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

    public void register(User user, String ... roleNames) {
        user.setPassword(encryptPassword(user.getPassword()));
        Set<Role> roles = new HashSet<>();

        if (roleNames != null) {
            for (String roleName : roleNames) {
                Role role = roleRepository.findByName(roleName);
                roles.add(role);
            }
        } else {
            roles.add(roleRepository.findByName("USER"));
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    public User me(Principal principal) {
        return userRepository.findByUsername(principal.getName());
    }
}
