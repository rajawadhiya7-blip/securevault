package com.securevault.securevault.controller;

import com.securevault.securevault.model.Secret;
import com.securevault.securevault.service.SecretService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "2. Secrets", description = "Secret management endpoints")
@RestController
@RequestMapping("/api/secrets")
public class SecretController {

    private final SecretService secretService;

    public SecretController(SecretService secretService) {
        this.secretService = secretService;
    }

    @PostMapping
    public ResponseEntity<?> createSecret(
            @RequestBody Map<String, String> request,
            Principal principal) {

        String name = request.get("name");
        String value = request.get("value");
        String type = request.get("type");
        String expiresAtStr = request.get("expiresAt");

        LocalDateTime expiresAt = expiresAtStr != null ?
                LocalDateTime.parse(expiresAtStr) : null;

        try {
            Secret secret = secretService.createSecret(
                    name, value, type,
                    principal.getName(), expiresAt);

            return ResponseEntity.ok(Map.of(
                    "id", secret.getId(),
                    "name", secret.getName(),
                    "type", secret.getType(),
                    "createdAt", secret.getCreatedAt().toString()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getUserSecrets(Principal principal) {

        List<Secret> secrets = secretService.getUserSecrets(principal.getName());

        List<Map<String, Object>> response = secrets.stream()
                .map(s -> Map.of(
                        "id", (Object) s.getId(),
                        "name", s.getName(),
                        "type", s.getType(),
                        "createdAt", s.getCreatedAt().toString(),
                        "expiresAt", s.getExpiresAt() != null ?
                                s.getExpiresAt().toString() : "never"
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/decrypt")
    public ResponseEntity<?> getDecryptedSecret(
            @PathVariable Long id,
            Principal principal) {

        try {
            String value = secretService.getDecryptedSecret(id, principal.getName());
            return ResponseEntity.ok(Map.of("value", value));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateSecret(
            @PathVariable Long id,
            @RequestBody Map<String, String> request,
            Principal principal) {

        try {
            Secret updated = secretService.updateSecret(
                    id, request.get("value"),
                    principal.getName());

            return ResponseEntity.ok(Map.of(
                    "id", updated.getId(),
                    "name", updated.getName(),
                    "message", "Secret updated successfully"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSecret(
            @PathVariable Long id,
            Principal principal) {

        try {
            secretService.deleteSecret(id, principal.getName());
            return ResponseEntity.ok(Map.of("message", "Secret deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}