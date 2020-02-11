package com.marcelo.studentsystem.controller;

import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.util.List;

@RestController
@RequestMapping("/students")
class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<Student>> findAll() {
        List<Student> all = studentService.findAll();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> findAll(@PathVariable Long id) {
        Student student = studentService.find(id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping("/{id}/classes")
    public List<ClassOfStudents> findAllClasses(@PathVariable Long id) {
        return studentService.findAllClassesOfStudent(id);
    }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        student.setId(null);
        Student createdStudent = studentService.create(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @PostMapping("/{studentId}/class/{classId}")
    public ResponseEntity<Student> addStudentToClass(@PathVariable Long studentId, @PathVariable Long classId) {
        studentService.addStudentToClass(studentId, classId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> edit(@Valid @RequestBody Student newStudent, @PathVariable Long id) {
        Student editStudent = studentService.edit(newStudent, id);
        return new ResponseEntity<>(editStudent, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        studentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
