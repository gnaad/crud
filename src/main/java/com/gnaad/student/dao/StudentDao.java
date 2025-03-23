package com.gnaad.student.dao;

import com.gnaad.student.model.Student;
import com.gnaad.student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StudentDao {

    @Autowired
    StudentRepository studentRepository;

    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(int id, Student student) {
        Optional<Student> currentStudent = studentRepository.findById(id);
        currentStudent.map(entity -> {
            entity.setFirstName(student.getFirstName());
            entity.setLastName(student.getLastName());
            entity.setSex(student.getSex());
            entity.setAge(student.getAge());
            entity.setEmail(student.getEmail());
            entity.setAddress(student.getAddress());
            entity.setFullName(student.getFullName());
            return studentRepository.save(entity);
        });
        return currentStudent.orElse(null);
    }

    public Boolean deleteStudent(int id) {
        Optional<Student> optional = studentRepository.findById(id);
        optional.ifPresent((value) -> studentRepository.delete(optional.get()));
        return optional.isPresent();
    }
}