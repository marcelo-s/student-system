package com.marcelo.studentsystem.controller;

import com.marcelo.studentsystem.exception.EntityIdNotFoundException;
import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.service.ClassOfStudentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * Controller class for classOfStudents
 * <p>
 * Root of the api is defined in @RequestMapping
 */
@RestController
@RequestMapping("/classes")
class ClassOfStudentsController {

    /**
     * Injection of service to handle operations on classOfStudents
     */
    @Autowired
    private ClassOfStudentsService classOfStudentsService;

    /**
     * Finds all the classOfStudents
     * @return list of all classOfStudents
     */
    @GetMapping
    public List<ClassOfStudents> findAll() {
        return classOfStudentsService.findAll();
    }

    /**
     * Find a specific classOfStudents
     * @param id the id of the classOfStudents to find
     * @return the classOfStudents with the id provided as a param
     * @throws EntityIdNotFoundException if the id provided as a param does not exist
     */
    @GetMapping("/{id}")
    public ClassOfStudents find(@PathVariable Long id) {
        return classOfStudentsService.find(id);
    }

    /**
     * Finds all students of a specific classOfStudents
     * @param id the id of the classOfStudents
     * @return the list of students that attend a classOfStudents
     * @throws EntityIdNotFoundException if the id provided as a param does not exist
     */
    @GetMapping("/{id}/students")
    public List<Student> findAllClasses(@PathVariable Long id) {
        return classOfStudentsService.findAllStudentsOfClass(id);
    }

    /**
     * Creates a classOfStudents
     * @param classOfStudents the classOfStudents to create
     * @return the created classOfStudents
     * @throws ConstraintViolationException if the classOfStudents provided as a param is invalid
     * @throws MethodArgumentNotValidException if the classOfStudents provided as a param is invalid
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClassOfStudents create(@Valid @RequestBody ClassOfStudents classOfStudents) {
        classOfStudents.setId(null);
        return classOfStudentsService.create(classOfStudents);
    }

    /**
     * Updates a classOfStudents
     * @param newClassOfStudents the classOfStudents to update
     * @param id the id of the classOfStudents to update
     * @throws ConstraintViolationException if the classOfStudents provided as a param is invalid
     * @throws MethodArgumentNotValidException if the classOfStudents provided as a param is invalid
     * @throws EntityIdNotFoundException if the id of classOfStudents provided as a param does not exist in the database
     */
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void edit(@Valid @RequestBody ClassOfStudents newClassOfStudents, @NotBlank @PathVariable Long id) {
        classOfStudentsService.edit(newClassOfStudents, id);
    }

    /**
     * Deletes a classOfStudents
     * @param id the id of the classOfStudents to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        classOfStudentsService.delete(id);
    }
}
