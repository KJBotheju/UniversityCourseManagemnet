package com.erp.universitycoursemanagement.service;

import com.erp.universitycoursemanagement.model.*;
import com.erp.universitycoursemanagement.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EnrollmentService {

    private final EnrollmentRepository enrollments;
    private final StudentRepository students;
    private final CourseRepository courses;

    public EnrollmentService(EnrollmentRepository enrollments, StudentRepository students, CourseRepository courses) {
        this.enrollments = enrollments;
        this.students = students;
        this.courses = courses;
    }

    public Enrollment enroll(Long studentId, Long courseId) {
        Student s = students.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));
        Course c = courses.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        if (enrollments.countByCourseId(courseId) >= c.getCapacity())
            throw new IllegalStateException("Course capacity reached");

        enrollments.findByStudentAndCourse(s, c)
                .ifPresent(e -> { throw new IllegalStateException("Already enrolled"); });

        // No Lombok builder here – always compiles
        Enrollment e = new Enrollment();
        e.setStudent(s);
        e.setCourse(c);
        e.setEnrolledAt(LocalDateTime.now());
        return enrollments.save(e);
    }

    public Enrollment grade(Long enrollmentId, Grade grade) {
        Enrollment e = enrollments.findById(enrollmentId)
                .orElseThrow(() -> new IllegalArgumentException("Enrollment not found: " + enrollmentId));
        e.setGrade(grade);
        return e;
    }

    public List<Enrollment> byStudent(Long studentId) { return enrollments.findByStudentId(studentId); }
    public List<Enrollment> byCourse(Long courseId) { return enrollments.findByCourseId(courseId); }
    public void drop(Long enrollmentId) { enrollments.deleteById(enrollmentId); }

    public double gpaForStudent(Long studentId) {
        List<Enrollment> list = byStudent(studentId);
        int totalCredits = list.stream()
                .filter(e -> e.getGrade() != null)
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
        if (totalCredits == 0) return 0.0;

        double totalPoints = list.stream()
                .filter(e -> e.getGrade() != null)
                .mapToDouble(e -> e.getCourse().getCredits() * e.getGrade().getPoints())
                .sum();
        return totalPoints / totalCredits;
    }
}