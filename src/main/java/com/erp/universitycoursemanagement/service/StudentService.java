package com.erp.universitycoursemanagement.service;

import com.erp.universitycoursemanagement.model.Student;
import com.erp.universitycoursemanagement.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class StudentService {

    private final StudentRepository students;

    public StudentService(StudentRepository students) { this.students = students; }

    public Page<Student> list(Pageable pageable) { return students.findAll(pageable); }
    public Student create(Student s) { return students.save(s); }

    public Student get(Long id) {
        return students.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found: " + id));
    }

    public Student update(Long id, Student s) {
        Student e = get(id);
        e.setFirstName(s.getFirstName());
        e.setLastName(s.getLastName());
        e.setEmail(s.getEmail());
        e.setIndexNumber(s.getIndexNumber());
        return e;
    }

    public void delete(Long id) { students.deleteById(id); }
}