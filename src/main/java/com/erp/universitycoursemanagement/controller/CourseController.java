package com.erp.universitycoursemanagement.controller;

import com.erp.universitycoursemanagement.model.Course;
import com.erp.universitycoursemanagement.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService service;
    public CourseController(CourseService service) { this.service = service; }

    @GetMapping public Page<Course> list(Pageable pageable) {
        return service.list(pageable);
    }

    @PostMapping public Course create(@Valid @RequestBody Course course) {
        return service.create(course);
    }

    @GetMapping("/{id}") public Course get(@PathVariable Long id) {
        return service.get(id);

    }

    @PutMapping("/{id}") public Course update(@PathVariable Long id, @Valid @RequestBody Course course) {
        return service.update(id, course);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}