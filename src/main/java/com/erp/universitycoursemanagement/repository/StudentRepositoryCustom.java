package com.erp.universitycoursemanagement.repository;

import com.erp.universitycoursemanagement.model.Student;
import java.util.List;

public interface StudentRepositoryCustom {
    List<Student> search(String q);
}