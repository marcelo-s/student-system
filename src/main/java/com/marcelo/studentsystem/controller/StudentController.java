package com.marcelo.studentsystem.controller;

import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/students")
class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public Student find(@PathVariable Long id) {
        return studentService.find(id);
    }

    @GetMapping("/{id}/classes")
    public List<ClassOfStudents> findAllClasses(@PathVariable Long id) {
        return studentService.findAllClassesOfStudent(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@Valid @RequestBody Student student) {
        student.setId(null);
        return studentService.create(student);
    }

    @PostMapping("/{studentId}/class/{classId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addStudentToClass(@PathVariable Long studentId, @PathVariable Long classId) {
        studentService.addStudentToClass(studentId, classId);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@Valid @RequestBody Student newStudent, @PathVariable Long id) {
        studentService.edit(newStudent, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
