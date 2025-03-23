package com.gnaad.student.controller;

import com.gnaad.student.model.Student;
import com.gnaad.student.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public ResponseEntity<Object> getStudents() {
        try {
            return ResponseEntity.ok(studentService.getAllStudent());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch student. Issue is ".concat(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudentById(@PathVariable int id) {
        try {
            return ResponseEntity.ok(studentService.getStudentById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch student by id. Issue is ".concat(e.getMessage()));
        }
    }

    @PostMapping("/")
    public ResponseEntity<Object> saveStudent(@RequestBody Student student) {
        try {
            return new ResponseEntity<>(studentService.saveStudent(student), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save student. Issue is ".concat(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable Integer id, @RequestBody Student student) {
        try {
            return new ResponseEntity<>(studentService.updateStudent(id, student), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update student. Issue is ".concat(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable Integer id) {
        try {
            if (studentService.deleteStudent(id)) {
                return ResponseEntity.status(HttpStatus.OK).body("Student deleted successfully");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No Student found with Id ".concat(id.toString()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete student. Issue is ".concat(e.getMessage()));
        }
    }
}