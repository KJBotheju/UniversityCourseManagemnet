package com.erp.universitycoursemanagement.repository.impl;

import com.erp.universitycoursemanagement.model.Student;
import com.erp.universitycoursemanagement.repository.StudentRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StudentRepositoryImpl implements StudentRepositoryCustom {

    @PersistenceContext private EntityManager em;

    @Override
    public List<Student> search(String q) {
        String like = "%" + q.toLowerCase() + "%";
        return em.createQuery("""
                SELECT s FROM Student s
                WHERE LOWER(s.firstName) LIKE :q OR LOWER(s.lastName) LIKE :q
                      OR LOWER(s.indexNumber) LIKE :q OR LOWER(s.email) LIKE :q
                ORDER BY s.lastName, s.firstName
                """, Student.class)
                .setParameter("q", like)
                .getResultList();
    }
}