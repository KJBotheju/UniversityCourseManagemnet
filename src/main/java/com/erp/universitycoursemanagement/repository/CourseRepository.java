package com.erp.universitycoursemanagement.repository;

import com.erp.universitycoursemanagement.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long>, CourseRepositoryCustom {
    Optional<Course> findByCode(String code);
    boolean existsByCode(String code);
}