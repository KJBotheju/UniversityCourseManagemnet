package com.erp.universitycoursemanagement.controller;

import com.erp.universitycoursemanagement.model.Enrollment;
import com.erp.universitycoursemanagement.model.Grade;
import com.erp.universitycoursemanagement.service.EnrollmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService service;
    public EnrollmentController(EnrollmentService service) { this.service = service; }

    @PostMapping("/enroll")
    public Enrollment enroll(@RequestParam Long studentId, @RequestParam Long courseId) {
        return service.enroll(studentId, courseId);
    }

    @PatchMapping("/{id}/grade")
    public Enrollment grade(@PathVariable Long id, @RequestParam Grade grade) {
        return service.grade(id, grade);
    }

    @GetMapping("/by-student/{studentId}") public List<Enrollment> byStudent(@PathVariable Long studentId) {
        return service.byStudent(studentId);
    }

    @GetMapping("/by-course/{courseId}") public List<Enrollment> byCourse(@PathVariable Long courseId) {
        return service.byCourse(courseId);
    }

    @DeleteMapping("/{id}") public void drop(@PathVariable Long id) {
        service.drop(id);
    }

    @GetMapping("/gpa/{studentId}") public double gpa(@PathVariable Long studentId) {
        return service.gpaForStudent(studentId);
    }
}