package com.marcelo.studentsystem.repository;

import com.marcelo.studentsystem.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for the students
 */
public interface StudentRepository extends JpaRepository<Student, Long> {
}
