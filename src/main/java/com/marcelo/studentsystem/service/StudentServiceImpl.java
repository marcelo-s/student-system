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
import java.util.ArrayList;
import java.util.List;

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
        List<ClassOfStudentsProjection> attendances = classAttendanceRepository.findAllByStudentId(id);
        List<ClassOfStudents> classes = new ArrayList<>();
        for (ClassOfStudentsProjection attendance : attendances) {
            classes.add(attendance.getClassOfStudents());
        }
        return classes;
    }

    @Override
    public Student create(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student edit(Student newStudent, Long id) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setFirstName(newStudent.getFirstName());
                    student.setLastName(newStudent.getLastName());
                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        classAttendanceRepository.deleteAllByStudentId(id);
        studentRepository.deleteById(id);
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
