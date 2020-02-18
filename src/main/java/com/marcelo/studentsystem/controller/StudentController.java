package com.marcelo.studentsystem.controller;

import com.marcelo.studentsystem.exception.EntityIdNotFoundException;
import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.List;

/**
 * Controller class for students
 * <p>
 * Root of the api is defined in @RequestMapping
 */
@RestController
@RequestMapping("/students")
class StudentController {

    /**
     * Injection of service to handle operations on students
     */
    @Autowired
    private StudentService studentService;

    /**
     * Finds all the students
     * @return list of all students
     */
    @GetMapping
    public List<Student> findAll() {
        return studentService.findAll();
    }

    /**
     * Finds a specific student
     * @param id the of the student to find
     * @return the student with the id provided as a parameter
     * @throws EntityIdNotFoundException if the id provided as a param does not exist
     */
    @GetMapping("/{id}")
    public Student find(@PathVariable Long id) {
        return studentService.find(id);
    }

    /**
     * Finds all classOfStudents of a specific student
     * @param id the id of the student
     * @return the list of classOfStudents that the student attends
     * @throws EntityIdNotFoundException if the id provided as a param does not exist
     */
    @GetMapping("/{id}/classes")
    public List<ClassOfStudents> findAllClasses(@PathVariable Long id) {
        return studentService.findAllClassesOfStudent(id);
    }

    /**
     * Creates a student
     * @param student the student to create
     * @return the created student
     * @throws ConstraintViolationException if the student provided as a param is invalid
     * @throws MethodArgumentNotValidException if the student provided as a param is invalid
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student create(@Valid @RequestBody Student student) {
        student.setId(null);
        return studentService.create(student);
    }

    /**
     * Assigns a student to a classOfStudents
     * @param studentId the id of the student
     * @param classId the id of the class
     * @throws EntityIdNotFoundException if the student or classOfStudents provided as a param does not exist in the database
     */
    @PostMapping("/{studentId}/class/{classId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addStudentToClass(@PathVariable Long studentId, @PathVariable Long classId) {
        studentService.addStudentToClass(studentId, classId);
    }

    /**
     * Updates a student
     * @param newStudent the student to update
     * @param id the id of the student to update
     * @throws ConstraintViolationException if the student provided as a param is invalid
     * @throws MethodArgumentNotValidException if the student provided as a param is invalid
     * @throws EntityIdNotFoundException if the student or classOfStudents id provided as a param does not exist in the database
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@Valid @RequestBody Student newStudent, @PathVariable Long id) {
        studentService.edit(newStudent, id);
    }

    /**
     * Deletes a student
     * @param id the id of the student to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        studentService.delete(id);
    }
}
