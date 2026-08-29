package com.securevault.securevault.service;

import com.securevault.securevault.model.Secret;
import com.securevault.securevault.repository.SecretRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SecretService {

    private final SecretRepository secretRepository;
    private final EncryptionService encryptionService;
    private final AuditLogService auditLogService;

    public SecretService(SecretRepository secretRepository,
                         EncryptionService encryptionService,
                         AuditLogService auditLogService) {
        this.secretRepository = secretRepository;
        this.encryptionService = encryptionService;
        this.auditLogService = auditLogService;
    }

    public Secret createSecret(String name, String value, String type,
                               String ownerUsername, LocalDateTime expiresAt,
                               String ipAddress) {
        if (secretRepository.existsByNameAndOwnerUsername(name, ownerUsername)) {
            throw new RuntimeException("Secret with this name already exists");
        }

        Secret secret = new Secret();
        secret.setName(name);
        secret.setEncryptedValue(encryptionService.encrypt(value));
        secret.setType(type);
        secret.setOwnerUsername(ownerUsername);
        secret.setExpiresAt(expiresAt);

        Secret saved = secretRepository.save(secret);

        auditLogService.log(ownerUsername, "CREATE", name, ipAddress,
                "Created secret of type: " + type);

        return saved;
    }

    public List<Secret> getUserSecrets(String ownerUsername, String ipAddress) {
        auditLogService.log(ownerUsername, "LIST", "ALL", ipAddress,
                "Listed all secrets");
        return secretRepository.findByOwnerUsername(ownerUsername);
    }

    public String getDecryptedSecret(Long id, String ownerUsername, String ipAddress) {
        Secret secret = secretRepository.findByIdAndOwnerUsername(id, ownerUsername)
                .orElseThrow(() -> new RuntimeException("Secret not found"));

        if (secret.getExpiresAt() != null &&
                secret.getExpiresAt().isBefore(LocalDateTime.now())) {
            auditLogService.log(ownerUsername, "DECRYPT_FAILED", secret.getName(),
                    ipAddress, "Secret has expired");
            throw new RuntimeException("Secret has expired");
        }

        auditLogService.log(ownerUsername, "DECRYPT", secret.getName(),
                ipAddress, "Secret decrypted successfully");

        return encryptionService.decrypt(secret.getEncryptedValue());
    }

    @Transactional
    public void deleteSecret(Long id, String ownerUsername, String ipAddress) {
        Secret secret = secretRepository.findByIdAndOwnerUsername(id, ownerUsername)
                .orElseThrow(() -> new RuntimeException("Secret not found"));

        auditLogService.log(ownerUsername, "DELETE", secret.getName(),
                ipAddress, "Secret deleted");

        secretRepository.deleteByIdAndOwnerUsername(id, ownerUsername);
    }

    public Secret updateSecret(Long id, String newValue,
                               String ownerUsername, String ipAddress) {
        Secret secret = secretRepository.findByIdAndOwnerUsername(id, ownerUsername)
                .orElseThrow(() -> new RuntimeException("Secret not found"));

        secret.setEncryptedValue(encryptionService.encrypt(newValue));
        Secret updated = secretRepository.save(secret);

        auditLogService.log(ownerUsername, "UPDATE", secret.getName(),
                ipAddress, "Secret value updated");

        return updated;
    }
}