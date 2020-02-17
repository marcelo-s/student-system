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
        return classAttendanceRepository.findAllByClassOfStudentsId(id)
                .stream()
                .map(StudentProjection::getStudent)
                .collect(Collectors.toList());
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
    public void edit(ClassOfStudents newClassOfStudents, Long id) {
        classOfStudentsRepository.findById(id)
                .ifPresentOrElse(
                        (currentClassOfStudents) -> editClassOfStudents(currentClassOfStudents, newClassOfStudents),
                        () -> {
                            throw new EntityIdNotFoundException(id, Constants.ENTITY_CLASS);
                        });
    }

    private void editClassOfStudents(ClassOfStudents currentClassOfStudents, ClassOfStudents newClassOfStudents) {
        currentClassOfStudents.setTitle(newClassOfStudents.getTitle());
        currentClassOfStudents.setDescription(newClassOfStudents.getDescription());
        classOfStudentsRepository.save(currentClassOfStudents);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        classAttendanceRepository.deleteAllByClassOfStudentsId(id);
        classOfStudentsRepository.deleteById(id);
    }

}
