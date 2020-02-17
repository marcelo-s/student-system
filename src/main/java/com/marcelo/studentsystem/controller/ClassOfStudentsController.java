package com.marcelo.studentsystem.controller;

import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.service.ClassOfStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/classes")
class ClassOfStudentsController {

    @Autowired
    private ClassOfStudentsService classOfStudentsService;

    @GetMapping
    public List<ClassOfStudents> findAll() {
        return classOfStudentsService.findAll();
    }

    @GetMapping("/{id}")
    public ClassOfStudents findAll(@PathVariable Long id) {
        return classOfStudentsService.find(id);
    }

    @GetMapping("/{id}/students")
    public List<Student> findAllClasses(@PathVariable Long id) {
        return classOfStudentsService.findAllStudentsOfClass(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClassOfStudents create(@Valid @RequestBody ClassOfStudents classOfStudents) {
        classOfStudents.setId(null);
        return classOfStudentsService.create(classOfStudents);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@Valid @RequestBody ClassOfStudents newClassOfStudents, @NotBlank @PathVariable Long id) {
        classOfStudentsService.edit(newClassOfStudents, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        classOfStudentsService.delete(id);
    }
}
