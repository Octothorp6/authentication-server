package com.cobra.authenticationserver.controller;

import com.cobra.authenticationserver.dto.User;
import com.cobra.authenticationserver.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    @PostMapping("/register/{roleName}")
    public User register(@RequestBody @Valid User user, @PathVariable String roleName) {
        return registrationService.register(user, roleName.toUpperCase());
    }
}
