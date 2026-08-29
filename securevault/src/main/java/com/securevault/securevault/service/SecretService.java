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

    public SecretService(SecretRepository secretRepository,
                         EncryptionService encryptionService) {
        this.secretRepository = secretRepository;
        this.encryptionService = encryptionService;
    }

    public Secret createSecret(String name, String value, String type,
                               String ownerUsername, LocalDateTime expiresAt) {
        if (secretRepository.existsByNameAndOwnerUsername(name, ownerUsername)) {
            throw new RuntimeException("Secret with this name already exists");
        }

        Secret secret = new Secret();
        secret.setName(name);
        secret.setEncryptedValue(encryptionService.encrypt(value));
        secret.setType(type);
        secret.setOwnerUsername(ownerUsername);
        secret.setExpiresAt(expiresAt);

        return secretRepository.save(secret);
    }

    public List<Secret> getUserSecrets(String ownerUsername) {
        return secretRepository.findByOwnerUsername(ownerUsername);
    }

    public String getDecryptedSecret(Long id, String ownerUsername) {
        Secret secret = secretRepository.findByIdAndOwnerUsername(id, ownerUsername)
                .orElseThrow(() -> new RuntimeException("Secret not found"));

        if (secret.getExpiresAt() != null &&
                secret.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Secret has expired");
        }

        return encryptionService.decrypt(secret.getEncryptedValue());
    }

    @Transactional
    public void deleteSecret(Long id, String ownerUsername) {
        if (!secretRepository.findByIdAndOwnerUsername(id, ownerUsername).isPresent()) {
            throw new RuntimeException("Secret not found");
        }
        secretRepository.deleteByIdAndOwnerUsername(id, ownerUsername);
    }

    public Secret updateSecret(Long id, String newValue, String ownerUsername) {
        Secret secret = secretRepository.findByIdAndOwnerUsername(id, ownerUsername)
                .orElseThrow(() -> new RuntimeException("Secret not found"));

        secret.setEncryptedValue(encryptionService.encrypt(newValue));
        return secretRepository.save(secret);
    }
}