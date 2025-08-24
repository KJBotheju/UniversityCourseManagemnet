package com.erp.universitycoursemanagement.repository;

import com.erp.universitycoursemanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>, StudentRepositoryCustom {
    Optional<Student> findByIndexNumber(String indexNumber);
    boolean existsByIndexNumber(String indexNumber);
}