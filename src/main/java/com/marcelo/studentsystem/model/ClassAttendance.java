package com.marcelo.studentsystem.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Entity class for classAttendance
 * This class is created as a separated class that models a student to a class relationship
 */
@Data
@Entity
@NoArgsConstructor
public class ClassAttendance {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private ClassOfStudents classOfStudents;

}
