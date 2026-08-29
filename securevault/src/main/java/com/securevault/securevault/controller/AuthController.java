package com.securevault.securevault.controller;

import com.securevault.securevault.dto.LoginRequest;
import com.securevault.securevault.dto.RegisterRequest;
import com.securevault.securevault.service.AuthService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "1. Authentication", description = "Register and login endpoints")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register( @Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
    @PostMapping("/register-admin")
    public String registerAdmin(@Valid @RequestBody RegisterRequest request) {
        return authService.registerAdmin(request);
    }
}
