package com.marcelo.studentsystem.model;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
public class ClassOfStudents {

    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "Title is mandatory")
    @Size(min=5, message="Title should have at least 5 characters")
    private String title;
    @NotBlank(message = "description is mandatory")
    private String description;

    @OneToMany(mappedBy = "classOfStudents")
    @JsonIgnore
    private Set<ClassAttendance> attendance;

    public ClassOfStudents(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
