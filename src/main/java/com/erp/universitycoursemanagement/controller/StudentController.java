package com.erp.universitycoursemanagement.controller;

import com.erp.universitycoursemanagement.model.Student;
import com.erp.universitycoursemanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) { this.service = service; }

    @GetMapping public Page<Student> list(Pageable pageable) {
        return service.list(pageable);
    }

    @PostMapping public Student create(@Valid @RequestBody Student student) {
        return service.create(student);
    }

    @GetMapping("/{id}") public Student get(@PathVariable Long id) {
        return service.get(id);
    }

    @PutMapping("/{id}") public Student update(@PathVariable Long id, @Valid @RequestBody Student student) {
        return service.update(id, student);
    }

    @DeleteMapping("/{id}") public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}