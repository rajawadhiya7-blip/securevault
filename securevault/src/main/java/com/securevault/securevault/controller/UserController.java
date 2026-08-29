package com.securevault.securevault.controller;

import com.securevault.securevault.model.AuditLog;
import com.securevault.securevault.service.AuditLogService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "3. User", description = "User profile endpoints")
@RestController
@RequestMapping("/api")
public class UserController {

    private final AuditLogService auditLogService;

    public UserController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping("/user/profile")
    public String profile() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return "Hello " + username + "! Your JWT is valid and working.";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "Welcome to the Admin Dashboard!";
    }

    @GetMapping("/admin/audit-logs")
    public ResponseEntity<List<AuditLog>> getAllAuditLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/admin/audit-logs/{username}")
    public ResponseEntity<List<AuditLog>> getUserAuditLogs(
            @PathVariable String username) {
        return ResponseEntity.ok(auditLogService.getUserLogs(username));
    }
}