package com.marcelo.studentsystem.service;

import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;

import java.util.List;

public interface ClassOfStudentsService {
    List<ClassOfStudents> findAll();

    List<Student> findAllStudentsOfClass(Long id);

    ClassOfStudents find(Long id);

    ClassOfStudents create(ClassOfStudents classOfStudents);

    void edit(ClassOfStudents classOfStudents, Long id);

    void delete(Long id);
}
