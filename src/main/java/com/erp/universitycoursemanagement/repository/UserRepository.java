package com.erp.universitycoursemanagement.repository;

import com.erp.universitycoursemanagement.model.User;
import com.erp.universitycoursemanagement.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndStatus(String username, Status status);
    Optional<User> findByEmailAndStatus(String email, Status status);
    boolean existsByUsernameAndStatus(String username, Status status);
    boolean existsByEmailAndStatus(String email, Status status);
}
