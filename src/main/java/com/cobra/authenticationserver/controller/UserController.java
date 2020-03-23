package com.cobra.authenticationserver.controller;

import com.cobra.authenticationserver.dto.User;
import com.cobra.authenticationserver.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {
    @Autowired
    UserRegistrationService registrationService;

    @GetMapping("/user/me")
    public User me(Principal principal) {
        return registrationService.me(principal);
    }

    @PostMapping("/encrypt")
    public String encrypt(@RequestBody String password) {
        return registrationService.encryptPassword(password);
    }
}
