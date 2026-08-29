package com.securevault.securevault.repository;

import com.securevault.securevault.model.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecretRepository extends JpaRepository<Secret, Long> {

    List<Secret> findByOwnerUsername(String ownerUsername);

    Optional<Secret> findByIdAndOwnerUsername(Long id, String ownerUsername);

    boolean existsByNameAndOwnerUsername(String name, String ownerUsername);

    void deleteByIdAndOwnerUsername(Long id, String ownerUsername);
}