package com.marcelo.studentsystem.repository;

import com.marcelo.studentsystem.model.ClassAttendance;
import com.marcelo.studentsystem.model.ClassOfStudentsProjection;
import com.marcelo.studentsystem.model.StudentProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for classAttendance
 */
public interface ClassAttendanceRepository extends JpaRepository<ClassAttendance, Long> {

    /**
     * Finds all the classOfStudents for a specific student
     * @param id the id of the student
     * @return the list of classOfStudents as a projection
     */
    List<ClassOfStudentsProjection> findAllByStudentId(Long id);

    /**
     * Finds all the students for a specific classOfStudents
     * @param id the id of the classOfStudents
     * @return the list of students as a projection
     */
    List<StudentProjection> findAllByClassOfStudentsId(Long id);

    /**
     * Deletes all classAttendance of a specific student
     * @param id the id of the student
     */
    void deleteAllByStudentId(Long id);

    /**
     * Deletes all classAttendance of a specific classOfStudents
     * @param id the id of the classOfStudents
     */
    void deleteAllByClassOfStudentsId(Long id);
}
