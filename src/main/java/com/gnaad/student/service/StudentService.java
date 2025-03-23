package com.gnaad.student.service;

import com.gnaad.student.dao.StudentDao;
import com.gnaad.student.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentDao studentDao;

    public List<Student> getAllStudent() {
        return studentDao.getAllStudent();
    }

    public Student getStudentById(Integer id) {
        return studentDao.getStudentById(id);
    }

    public Student saveStudent(Student student) {
        return studentDao.saveStudent(student);
    }

    public Student updateStudent(Integer id, Student student) {
        return studentDao.updateStudent(id, student);
    }

    public Boolean deleteStudent(Integer id) {
        return studentDao.deleteStudent(id);
    }
}