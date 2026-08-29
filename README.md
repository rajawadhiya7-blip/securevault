# SecureVault

A production-grade secrets and credential management REST API developed during my internship at HCLTech. SecureVault allows teams to securely store, retrieve, and audit sensitive credentials such as API keys, passwords, and database connection strings.

## Live API
🔗 https://securevault-production-3b16.up.railway.app/swagger-ui.html

## Security Features
- **AES-256/GCM Encryption** — All secrets are encrypted at rest before storage
- **JWT Authentication** — Stateless authentication with role-based access control
- **Account Lockout** — Automatic lockout after repeated failed login attempts
- **IP Rate Limiting** — Protects against brute force attacks
- **Audit Logging** — Every secret access is recorded with timestamp, IP address, and action type
- **Secret Expiry** — Secrets can be configured with expiration dates

## Tech Stack
- Java 21 / Spring Boot 3.5
- Spring Security
- Spring Data JPA / Hibernate
- MySQL
- Railway (cloud deployment)

## API Documentation
Full API documentation available via Swagger UI at the live URL above.

## Architecture
The application follows a layered architecture — Controller → Service → Repository — with a dedicated encryption service handling all cryptographic operations, and an audit service that transparently logs all secret access operations.ocalhost:8081/swagger-ui.html`