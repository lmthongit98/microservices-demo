package com.tma.userservice.repository;

import com.tma.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}