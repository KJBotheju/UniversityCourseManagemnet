package com.erp.universitycoursemanagement.repository.impl;

import com.erp.universitycoursemanagement.model.Course;
import com.erp.universitycoursemanagement.repository.CourseRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CourseRepositoryImpl implements CourseRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Course> searchByTitleOrCode(String q) {
        String like = "%" + q.toLowerCase() + "%";
        return em.createQuery("""
                SELECT c FROM Course c
                WHERE LOWER(c.title) LIKE :q OR LOWER(c.code) LIKE :q
                ORDER BY c.code
                """, Course.class)
                .setParameter("q", like)
                .getResultList();
    }
}