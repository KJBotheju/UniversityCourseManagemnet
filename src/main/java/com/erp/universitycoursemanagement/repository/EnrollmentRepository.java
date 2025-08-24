package com.erp.universitycoursemanagement.repository;

import com.erp.universitycoursemanagement.model.Enrollment;
import com.erp.universitycoursemanagement.model.Student;
import com.erp.universitycoursemanagement.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudentId(Long studentId);
    List<Enrollment> findByCourseId(Long courseId);
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
    long countByCourseId(Long courseId);
}