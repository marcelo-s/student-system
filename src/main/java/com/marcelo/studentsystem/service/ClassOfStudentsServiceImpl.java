package com.marcelo.studentsystem.service;

import com.marcelo.studentsystem.exception.EntityIdNotFoundException;
import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.model.StudentProjection;
import com.marcelo.studentsystem.repository.ClassAttendanceRepository;
import com.marcelo.studentsystem.repository.ClassOfStudentsRepository;
import com.marcelo.studentsystem.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for classOfStudents operations
 */
@Service
public class ClassOfStudentsServiceImpl implements ClassOfStudentsService {

    @Autowired
    private ClassOfStudentsRepository classOfStudentsRepository;

    @Autowired
    private ClassAttendanceRepository classAttendanceRepository;

    @Override
    public List<ClassOfStudents> findAll() {
        return classOfStudentsRepository.findAll();
    }

    /**
     * Finds all the students that attends a class
     * @param id the id of the class
     * @return the list of students that attend the class with id provided as a parameter
     */
    @Override
    public List<Student> findAllStudentsOfClass(Long id) {
        classOfStudentsRepository.findById(id)
                .orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_CLASS));
        return classAttendanceRepository.findAllByClassOfStudentsId(id)
                .stream()
                .map(StudentProjection::getStudent)
                .collect(Collectors.toList());
    }

    /**
     * Finds a specific classOfStudents
     * @param id the id of the classOfStudents
     * @return the classOfStudents with id privided as a param
     * @throws EntityIdNotFoundException if the id of the classOfStudents does not exist on the database
     */
    @Override
    public ClassOfStudents find(Long id) {
        return classOfStudentsRepository.findById(id)
                .orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_CLASS));
    }

    /**
     * Creates a classOfStudents
     * @param classOfStudents the classOfStudents to be created
     * @return the classOfStudentsCreated
     */
    @Override
    public ClassOfStudents create(ClassOfStudents classOfStudents) {
        return classOfStudentsRepository.save(classOfStudents);
    }

    /**
     * Updates a classOfStudents
     * @param newClassOfStudents the classOfStudents to be updated
     * @param id the id of the classOfStudents to be updated
     * @throws EntityIdNotFoundException if the id of the classOfStudents does not exist on the database
     */
    @Override
    public void edit(ClassOfStudents newClassOfStudents, Long id) {
        classOfStudentsRepository.findById(id)
                .ifPresentOrElse(
                        (currentClassOfStudents) -> editClassOfStudents(currentClassOfStudents, newClassOfStudents),
                        () -> {
                            throw new EntityIdNotFoundException(id, Constants.ENTITY_CLASS);
                        });
    }

    /**
     * Updates the current classOfStudents with the information of the newClassOfStudents
     * @param currentClassOfStudents the classOfStudents as it is in the database
     * @param newClassOfStudents the classOfStudents with the information to be updated
     */
    private void editClassOfStudents(ClassOfStudents currentClassOfStudents, ClassOfStudents newClassOfStudents) {
        currentClassOfStudents.setTitle(newClassOfStudents.getTitle());
        currentClassOfStudents.setDescription(newClassOfStudents.getDescription());
        classOfStudentsRepository.save(currentClassOfStudents);
    }

    /**
     * Deletes a classOfStudents
     * <p>
     * The operation needs to be @Transactional as it has two operations:
     * <p>
     * First delete the student-classOfStudents attendance in the ClassAttendance table
     * <p>
     * Then delete the classOfStudents in the Student table
     * @param id the id of the classOfStudents to be deleted
     * @throws EntityIdNotFoundException if the id of the classOfStudents does not exist on the database
     */
    @Override
    @Transactional
    public void delete(Long id) {
        classOfStudentsRepository.findById(id)
                .ifPresentOrElse(
                        this::deleteClassOfStudents,
                        () -> {
                            throw new EntityIdNotFoundException(id, Constants.ENTITY_CLASS);
                        });
    }

    /**
     * Performs the two-step deletion of the student
     * First delete the student-classOfStudents attendance in the ClassAttendance table
     * <p>
     * Then delete the classOfStudents in the Student table
     * @param classOfStudents the classOfStudents to be deleted
     */
    private void deleteClassOfStudents(ClassOfStudents classOfStudents) {
        classAttendanceRepository.deleteAllByClassOfStudentsId(classOfStudents.getId());
        classOfStudentsRepository.deleteById(classOfStudents.getId());
    }

}
