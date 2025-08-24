package com.erp.universitycoursemanagement.repository;

import com.erp.universitycoursemanagement.model.Course;

import java.util.List;

public interface CourseRepositoryCustom {
    List<Course> searchByTitleOrCode(String q);
}