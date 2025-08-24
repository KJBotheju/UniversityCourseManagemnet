package com.erp.universitycoursemanagement.service;

import com.erp.universitycoursemanagement.model.Course;
import com.erp.universitycoursemanagement.repository.CourseRepository;
import com.erp.universitycoursemanagement.repository.EnrollmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class CourseService {

    private final CourseRepository courses;
    private final EnrollmentRepository enrollments;

    public CourseService(CourseRepository courses, EnrollmentRepository enrollments) {
        this.courses = courses; this.enrollments = enrollments;
    }

    public Page<Course> list(Pageable pageable) { return courses.findAll(pageable); }
    public Course create(Course c) { return courses.save(c); }

    public Course get(Long id) {
        return courses.findById(id).orElseThrow(() -> new IllegalArgumentException("Course not found: " + id));
    }

    public Course update(Long id, Course c) {
        Course e = get(id);
        e.setTitle(c.getTitle());
        e.setCode(c.getCode());
        e.setCredits(c.getCredits());
        e.setCapacity(c.getCapacity());
        return e;
    }

    public void delete(Long id) {
        if (enrollments.countByCourseId(id) > 0)
            throw new IllegalStateException("Cannot delete: students are enrolled");
        courses.deleteById(id);
    }
}