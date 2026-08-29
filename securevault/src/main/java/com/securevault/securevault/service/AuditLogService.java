package com.securevault.securevault.service;

import com.securevault.securevault.model.AuditLog;
import com.securevault.securevault.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(String username, String action, String secretName,
                    String ipAddress, String details) {
        AuditLog log = new AuditLog();
        log.setUsername(username);
        log.setAction(action);
        log.setSecretName(secretName);
        log.setIpAddress(ipAddress);
        log.setDetails(details);
        auditLogRepository.save(log);
    }

    public List<AuditLog> getUserLogs(String username) {
        return auditLogRepository.findByUsernameOrderByTimestampDesc(username);
    }

    public List<AuditLog> getSecretLogs(String secretName) {
        return auditLogRepository.findBySecretNameOrderByTimestampDesc(secretName);
    }

    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByTimestampDesc();
    }
}