package com.marcelo.studentsystem.service;

import com.marcelo.studentsystem.exception.EntityIdNotFoundException;
import com.marcelo.studentsystem.model.ClassAttendance;
import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.ClassOfStudentsProjection;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.repository.ClassAttendanceRepository;
import com.marcelo.studentsystem.repository.ClassOfStudentsRepository;
import com.marcelo.studentsystem.repository.StudentRepository;
import com.marcelo.studentsystem.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for students operations
 */
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassAttendanceRepository classAttendanceRepository;

    @Autowired
    private ClassOfStudentsRepository classOfStudentsRepository;

    @Override
    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    /**
     * Finds a specific student
     * @param id the id of the student to find
     * @return the student with the id provided as a param
     * @throws EntityIdNotFoundException if the id of the student does not exist on the database
     */
    @Override
    public Student find(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT));
    }

    /**
     * Finds all the classes of a specific student
     * @param id the id of the student
     * @return the list of all the classes where the student attends
     * @throws EntityIdNotFoundException if the id of the student does not exist on the database
     */
    @Override
    public List<ClassOfStudents> findAllClassesOfStudent(Long id) {
        studentRepository
                .findById(id)
                .orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT));
        return classAttendanceRepository.findAllByStudentId(id)
                .stream()
                .map(ClassOfStudentsProjection::getClassOfStudents)
                .collect(Collectors.toList());
    }

    /**
     * Creates a student
     * @param student the student to be created
     * @return the created student
     */
    @Override
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    /**
     * Updates a student
     * @param newStudent the student to be updated
     * @param id the ide of the student to be updated
     * @throws EntityIdNotFoundException if the id of the student does not exist on the database
     */
    @Override
    public void edit(Student newStudent, Long id) {
        studentRepository.findById(id)
                .ifPresentOrElse(student -> editStudent(student, newStudent),
                        () -> {
                            throw new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT);
                        });
    }

    /**
     * Updates a currentStudent with new information from the newStudent
     * @param currentStudent the student as it is in the database
     * @param newStudent the student with the information to be updated
     */
    private void editStudent(Student currentStudent, Student newStudent) {
        currentStudent.setFirstName(newStudent.getFirstName());
        currentStudent.setLastName(newStudent.getLastName());
        studentRepository.save(currentStudent);
    }

    /**
     * Deletes a student
     * <p>
     * The operation needs to be @Transactional as it has two operations:
     * <p>
     * First delete the student-classOfStudents attendance in the ClassAttendance table
     * <p>
     * Then delete the student in the Student table
     * @param id the id of the student to be deleted
     * @throws EntityIdNotFoundException if the id of the student does not exist on the database
     */
    @Override
    @Transactional
    public void delete(Long id) {
        studentRepository
                .findById(id)
                .ifPresentOrElse(
                        this::deleteStudent,
                        () -> {
                            throw new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT);
                        });
    }

    /**
     * Performs the two-step deletion of the student
     * First delete the student-classOfStudents attendance in the ClassAttendance table
     * <p>
     * Then delete the student in the Student table
     * @param student the student to be deleted
     */
    private void deleteStudent(Student student) {
        classAttendanceRepository.deleteAllByStudentId(student.getId());
        studentRepository.deleteById(student.getId());
    }

    /**
     * Adds a student to a classOfStudents
     * @param studentId the id of the student to add to a class
     * @param classId the id of the class where the student is to be added
     * @throws EntityIdNotFoundException if the id of the student or the id of the class does not exist on the database
     */
    @Override
    public void addStudentToClass(Long studentId, Long classId) {
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> new EntityIdNotFoundException(studentId, Constants.ENTITY_STUDENT));
        ClassOfStudents classOfStudents = classOfStudentsRepository
                .findById(classId)
                .orElseThrow(() -> new EntityIdNotFoundException(classId, Constants.ENTITY_CLASS));
        ClassAttendance classAttendance = new ClassAttendance();
        classAttendance.setStudent(student);
        classAttendance.setClassOfStudents(classOfStudents);
        classAttendanceRepository.save(classAttendance);
    }
}
