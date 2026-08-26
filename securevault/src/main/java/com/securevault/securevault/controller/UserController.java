package com.securevault.securevault.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    @GetMapping("/user/profile")
    public String profile() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return "Hello " + username + "! Your JWT is valid and working.";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return "Welcome Admin " + username + "! You have full access.";
    }
}