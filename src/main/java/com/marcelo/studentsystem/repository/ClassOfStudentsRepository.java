package com.marcelo.studentsystem.repository;

import com.marcelo.studentsystem.model.ClassOfStudents;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for classOfStudents
 */
public interface ClassOfStudentsRepository extends JpaRepository<ClassOfStudents, Long> {
}
