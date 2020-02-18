package com.marcelo.studentsystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.Set;

/**
 * Entity class for a student
 */
@Data
@Entity
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "First name is mandatory")
    private String firstName;
    @NotBlank(message = "Last name is mandatory")
    private String lastName;

    @OneToMany(mappedBy = "student")
    @JsonIgnore
    private Set<ClassAttendance> attendance;

    public Student(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
