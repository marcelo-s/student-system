package com.marcelo.studentsystem.service;

import com.marcelo.studentsystem.exception.EntityIdNotFoundException;
import com.marcelo.studentsystem.model.ClassAttendance;
import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.ClassOfStudentsProjection;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.repository.ClassAttendanceRepository;
import com.marcelo.studentsystem.repository.ClassOfStudentsRepository;
import com.marcelo.studentsystem.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RunWith(SpringRunner.class)
@DataJpaTest
class StudentServiceImplTest {

    @TestConfiguration
    static class EmployeeServiceImplTestContextConfiguration {

        @Bean
        public StudentService studentService() {
            return new StudentServiceImpl();
        }
    }

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private ClassAttendanceRepository classAttendanceRepository;

    @MockBean
    private ClassOfStudentsRepository classOfStudentsRepository;

    @Test
    void findAll() {
        Student student1 = new Student("john", "cena");
        Student student2 = new Student("stone", "cold");
        List<Student> students = Arrays.asList(student1, student2);

        Mockito.when(studentRepository.findAll())
                .thenReturn(students);

        List<Student> allStudents = studentService.findAll();
        assertEquals(2, allStudents.size());
    }

    @Test
    void find() {
        Student student1 = new Student("john", "cena");
        long id = 1;
        Mockito.when(studentRepository.findById(id))
                .thenReturn(Optional.of(student1));
        assertEquals("john", studentService.find(1L).getFirstName());
    }

    @Test
    void findNotFound() {
        long id = 1;
        Mockito.when(studentRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(EntityIdNotFoundException.class, () -> studentService.find(id));
    }

    @Test
    void findAllClassesOfStudent() {
        Student student1 = new Student("john", "cena");
        ClassOfStudents class1 = new ClassOfStudents("class1", "desc1");
        ClassOfStudents class2 = new ClassOfStudents("class2", "desc2");
        ClassOfStudents class3 = new ClassOfStudents("class3", "desc3");
        ClassOfStudentsProjection projection1 = () -> class1;
        ClassOfStudentsProjection projection2 = () -> class2;
        ClassOfStudentsProjection projection3 = () -> class3;


        Mockito.when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student1));

        List<ClassOfStudentsProjection> projections = Arrays.asList(projection1, projection2, projection3);
        Mockito.when(classAttendanceRepository.findAllByStudentId(1L))
                .thenReturn(projections);

        List<ClassOfStudents> allStudents = studentService.findAllClassesOfStudent(1L);
        assertEquals(3, allStudents.size());
    }

    @Test
    void findAllClassesOfStudentNotFound() {
        long id = 1;
        Mockito.when(studentRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(EntityIdNotFoundException.class, () -> studentService.findAllClassesOfStudent(id));
    }

    @Test
    void create() {
        Student newStudent = new Student("john", "cena");
        Mockito.when(studentRepository.save(newStudent))
                .thenReturn(newStudent);
        Student studentCreated = studentService.create(newStudent);
        assertEquals(newStudent, studentCreated);
    }

    @Test
    void edit() {
        Student student = new Student("john", "cena");
        long id = 1;
        Mockito.when(studentRepository.findById(1L))
                .thenReturn(Optional.of(student));
        Mockito.when(studentRepository.save(student))
                .thenReturn(student);
        studentService.edit(student, id);
        Mockito.verify(studentRepository, Mockito.atMostOnce()).save(student);
    }

    @Test
    void editStudentNotFound() {
        Student student = new Student("john", "cena");
        long id = 2;
        Mockito.when(studentRepository.findById(id))
                .thenReturn(Optional.empty());
        assertThrows(EntityIdNotFoundException.class, () -> studentService.edit(student, id));
    }

    @Test
    void delete() {
        studentService.delete(1L);
        InOrder inOrder = Mockito.inOrder(classAttendanceRepository, studentRepository);
        inOrder.verify(classAttendanceRepository).deleteAllByStudentId(1L);
        inOrder.verify(studentRepository).deleteById(1L);
    }

    @Test
    void addStudentToClass() {
        long studentId = 1;
        long classId = 1;

        Student student = new Student("john", "cena");
        ClassOfStudents classOfStudents = new ClassOfStudents("algorithms", "basic algorithms");
        ClassAttendance classAttendance = new ClassAttendance();
        classAttendance.setStudent(student);
        classAttendance.setClassOfStudents(classOfStudents);

        Mockito.when(studentRepository.findById(studentId))
                .thenReturn(Optional.of(student));
        Mockito.when(classOfStudentsRepository.findById(classId))
                .thenReturn(Optional.of(classOfStudents));

        studentService.addStudentToClass(studentId, classId);

        InOrder inOrder = Mockito.inOrder(classAttendanceRepository, studentRepository, classOfStudentsRepository);
        inOrder.verify(studentRepository).findById(studentId);
        inOrder.verify(classOfStudentsRepository).findById(classId);
        inOrder.verify(classAttendanceRepository).save(classAttendance);
    }
}