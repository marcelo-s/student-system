package com.marcelo.studentsystem.repository;

import com.marcelo.studentsystem.model.ClassAttendance;
import com.marcelo.studentsystem.model.ClassOfStudentsProjection;
import com.marcelo.studentsystem.model.StudentProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassAttendanceRepository extends JpaRepository<ClassAttendance, Long> {
    List<ClassOfStudentsProjection> findAllByStudentId(Long id);

    List<StudentProjection> findAllByClassOfStudentsId(Long id);

    void deleteAllByStudentId(Long id);

    void deleteAllByClassOfStudentsId(Long id);
}
