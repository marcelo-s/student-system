package com.marcelo.studentsystem.service;

import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;

import java.util.List;

public interface StudentService {
    List<Student> findAll();

    List<ClassOfStudents> findAllClassesOfStudent(Long id);

    Student find(Long id);

    Student create(Student student);

    Student edit(Student student, Long id);

    void delete(Long id);

    void addStudentToClass(Long studentId, Long classId);
}
