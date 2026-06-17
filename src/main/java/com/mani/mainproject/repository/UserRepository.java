package com.mani.mainproject.repository;

import com.mani.mainproject.entity.User;
import com.mani.mainproject.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    long countByStatus(UserStatus status);
}
