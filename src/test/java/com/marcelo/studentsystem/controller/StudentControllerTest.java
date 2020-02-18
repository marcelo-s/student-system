package com.marcelo.studentsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marcelo.studentsystem.exception.EntityIdNotFoundException;
import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import com.marcelo.studentsystem.service.StudentService;
import com.marcelo.studentsystem.utils.Constants;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StudentService studentService;

    @Test
    void findAll() throws Exception {
        Student student1 = new Student("john", "cena");
        Student student2 = new Student("john", "wick");

        List<Student> students = Arrays.asList(student1, student2);
        given(studentService.findAll()).willReturn(students);

        mvc.perform(MockMvcRequestBuilders.get("/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].firstName", is(student1.getFirstName())));
    }

    @Test
    void find() throws Exception {
        Student student1 = new Student("john", "cena");
        long id = 1;

        given(studentService.find(id)).willReturn(student1);

        mvc.perform(MockMvcRequestBuilders.get("/students/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is(student1.getLastName())));
    }

    @Test
    void findNotFound() throws Exception {
        long id = 1;
        String exceptionMessage = "Student id not found : " + id;
        given(studentService.find(id)).willThrow(new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT));

        mvc.perform(MockMvcRequestBuilders.get("/students/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(exceptionMessage)));
    }

    @Test
    void findAllClasses() throws Exception {
        long id = 1;
        ClassOfStudents class1 = new ClassOfStudents("class1", "desc1");
        ClassOfStudents class2 = new ClassOfStudents("class2", "desc2");
        List<ClassOfStudents> classesOfStudent = Arrays.asList(class1, class2);
        given(studentService.findAllClassesOfStudent(id)).willReturn(classesOfStudent);

        mvc.perform(MockMvcRequestBuilders.get("/students/{id}/classes", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(class1.getTitle())));
    }

    @Test
    void findAllClassesStudentNotFound() throws Exception {
        long id = 1;
        given(studentService.findAllClassesOfStudent(id)).willThrow(new EntityIdNotFoundException(id, Constants.ENTITY_STUDENT));
        String exceptionMessage = "Student id not found : " + id;
        mvc.perform(MockMvcRequestBuilders.get("/students/{id}/classes", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$", is(exceptionMessage)));
    }

    @Test
    void create() throws Exception {
        Student student1 = new Student("john", "cena");
        given(studentService.create(student1)).willReturn(student1);
        mvc.perform(MockMvcRequestBuilders
                .post("/students")
                .content(asJsonString(student1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(student1.getFirstName())));
    }

    @Test
    void createInvalidStudent() throws Exception {
        Student student1 = new Student();
        String lastNameError = "Last name is mandatory";
        String firstNameError = "First name is mandatory";
        Set<String> errors = new HashSet<>();
        errors.add(firstNameError);
        errors.add(lastNameError);
        given(studentService.create(student1)).willReturn(student1);
        mvc.perform(MockMvcRequestBuilders
                .post("/students")
                .content(asJsonString(student1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasSize(2)))
                .andExpect(jsonPath("$.errors[0]", in(errors)))
                .andExpect(jsonPath("$.errors[1]", in(errors)));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addStudentToClass() throws Exception {
        long studentId = 1;
        long classId = 1;
        mvc.perform(MockMvcRequestBuilders
                .post("/students/{studentId}/class/{classId}", studentId, classId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void addStudentToClassStudentNotFound() throws Exception {
        long studentId = 1;
        long classId = 1;
        doThrow(EntityIdNotFoundException.class).when(studentService).addStudentToClass(studentId, classId);
        mvc.perform(MockMvcRequestBuilders
                .post("/students/{studentId}/class/{classId}", studentId, classId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void edit() throws Exception {
        Student student1 = new Student("john", "cena");
        long id = 1;
        when(studentService.find(id)).thenReturn(student1);
        mvc.perform(MockMvcRequestBuilders
                .put("/students/{id}", id)
                .content(asJsonString(student1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void editWithNonExistentId() throws Exception {
        Student student1 = new Student("john", "cena");
        long id = 1;
        doThrow(EntityIdNotFoundException.class).when(studentService).edit(student1, id);
        mvc.perform(MockMvcRequestBuilders
                .put("/students/{id}", id)
                .content(asJsonString(student1))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/students/{id}", 1))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteWithNonExistentId() throws Exception {
        long id = 234;
        doThrow(EntityIdNotFoundException.class).when(studentService).delete(id);
        mvc.perform(MockMvcRequestBuilders.delete("/students/{id}", id))
                .andExpect(status().isNotFound());
        Mockito.verify(studentService, Mockito.atMostOnce()).delete(id);
    }
}