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
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Student> findAllStudentsOfClass(Long id) {
        classOfStudentsRepository.findById(id)
                .orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_CLASS));
        List<StudentProjection> attendances = classAttendanceRepository.findAllByClassOfStudentsId(id);
        List<Student> students = new ArrayList<>();
        for (StudentProjection attendance : attendances) {
            students.add(attendance.getStudent());
        }
        return students;
    }

    @Override
    public ClassOfStudents find(Long id) {
        return classOfStudentsRepository.findById(id)
                .orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_CLASS));
    }

    @Override
    public ClassOfStudents create(ClassOfStudents classOfStudents) {
        return classOfStudentsRepository.save(classOfStudents);
    }

    @Override
    public ClassOfStudents edit(ClassOfStudents newClassOfStudents, Long id) {
        return classOfStudentsRepository.findById(id)
                .map(classOfStudents -> {
                    classOfStudents.setTitle(newClassOfStudents.getTitle());
                    classOfStudents.setDescription(newClassOfStudents.getDescription());
                    return classOfStudentsRepository.save(classOfStudents);
                })
                .orElseThrow(() -> new EntityIdNotFoundException(id, Constants.ENTITY_CLASS));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        classAttendanceRepository.deleteAllByClassOfStudentsId(id);
        classOfStudentsRepository.deleteById(id);
    }

}
