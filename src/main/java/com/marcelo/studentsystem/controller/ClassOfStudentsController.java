package com.marcelo.studentsystem.controller;

import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.service.ClassOfStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/classes")
class ClassOfStudentsController {

    @Autowired
    private ClassOfStudentsService classOfStudentsService;

    @GetMapping
    public List<com.marcelo.studentsystem.model.ClassOfStudents> findAll() {
        return classOfStudentsService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassOfStudents> findAll(@PathVariable Long id) {
        ClassOfStudents classOfStudents = classOfStudentsService.find(id);
        return new ResponseEntity<>(classOfStudents, HttpStatus.OK);
    }

    @GetMapping("/{id}/students")
    public List<Student> findAllClasses(@PathVariable Long id) {
        return classOfStudentsService.findAllStudentsOfClass(id);
    }

    @PostMapping
    public ResponseEntity<ClassOfStudents> create(@Valid @RequestBody ClassOfStudents classOfStudents) {
        classOfStudents.setId(null);
        ClassOfStudents classOfStudentsCreated = classOfStudentsService.create(classOfStudents);
        return new ResponseEntity<>(classOfStudentsCreated, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassOfStudents> edit(@Valid @RequestBody ClassOfStudents newClassOfStudents, @NotBlank @PathVariable Long id) {
        ClassOfStudents editClass = classOfStudentsService.edit(newClassOfStudents, id);
        return new ResponseEntity<>(editClass, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        classOfStudentsService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
