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

    @Override
    public Student find(Long id) {
        return studentRepository.findById(id).orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT));
    }

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

    @Override
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public void edit(Student newStudent, Long id) {
        studentRepository.findById(id)
                .ifPresentOrElse(student -> editStudent(student, newStudent),
                        () -> {
                            throw new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT);
                        });
    }

    private void editStudent(Student currentStudent, Student newStudent) {
        currentStudent.setFirstName(newStudent.getFirstName());
        currentStudent.setLastName(newStudent.getLastName());
        studentRepository.save(currentStudent);
    }

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

    private void deleteStudent(Student student) {
        classAttendanceRepository.deleteAllByStudentId(student.getId());
        studentRepository.deleteById(student.getId());
    }

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
