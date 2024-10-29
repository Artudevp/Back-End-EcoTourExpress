package com.ecotourexpress.ecotourexpress.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecotourexpress.ecotourexpress.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    boolean existsByCorreo(String correo);
    boolean existsByUsername(String username);
}
