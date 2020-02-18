package com.marcelo.studentsystem.repository;

import com.marcelo.studentsystem.model.ClassAttendance;
import com.marcelo.studentsystem.model.ClassOfStudents;
import com.marcelo.studentsystem.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that initializes the database
 */
@Slf4j
@Configuration
public class LoadDB {

    /**
     *
     * @param studentRepository repository of students
     * @param classRepository repository of classOfStudents
     * @param attendanceRepository repository of attendanceOfStudents
     * @return a runner that initializes the script
     */
    @Bean
    CommandLineRunner initStudentsDatabase(StudentRepository studentRepository, ClassOfStudentsRepository classRepository, ClassAttendanceRepository attendanceRepository) {
        return args -> {

            // Create Students
            Student student1 = studentRepository.save(new Student("John", "Smith"));
            Student student2 = studentRepository.save(new Student("Beverly", "Payne"));
            Student student3 = studentRepository.save(new Student("Bobby", "Campbell"));
            Student student4 = studentRepository.save(new Student("Gerald", "Morgan"));
            Student student5 = studentRepository.save(new Student("Margaret", " Young"));

            // Create Classes
            ClassOfStudents class1 = classRepository.save(new ClassOfStudents("Algorithms", "Basic algorithms class"));
            ClassOfStudents class2 = classRepository.save(new ClassOfStudents("Databases", "Databases basics"));


            // Create joinTable Student - Classes
            List<ClassAttendance> attendanceList = new ArrayList<>();
            ClassAttendance attendance1 = new ClassAttendance();
            attendance1.setStudent(student1);
            attendance1.setClassOfStudents(class1);
            attendanceList.add(attendance1);

            ClassAttendance attendance11 = new ClassAttendance();
            attendance11.setStudent(student1);
            attendance11.setClassOfStudents(class2);
            attendanceList.add(attendance11);

            ClassAttendance attendance2 = new ClassAttendance();
            attendance2.setStudent(student2);
            attendance2.setClassOfStudents(class1);
            attendanceList.add(attendance2);

            ClassAttendance attendance3 = new ClassAttendance();
            attendance3.setStudent(student3);
            attendance3.setClassOfStudents(class1);
            attendanceList.add(attendance3);

            ClassAttendance attendance4 = new ClassAttendance();
            attendance4.setStudent(student4);
            attendance4.setClassOfStudents(class2);
            attendanceList.add(attendance4);

            ClassAttendance attendance5 = new ClassAttendance();
            attendance5.setStudent(student5);
            attendance5.setClassOfStudents(class2);
            attendanceList.add(attendance5);

            attendanceRepository.saveAll(attendanceList);

        };
    }

}
