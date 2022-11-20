package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentRecord> getStudentInfo(@PathVariable Long id) {
        StudentRecord studentRecord = studentService.findStudent(id);
        return ResponseEntity.ok(studentRecord);
    }

    @PostMapping
    public StudentRecord createStudent(@RequestBody StudentRecord studentRecord) {
        return studentService.addStudent(studentRecord);
    }

    @PutMapping("{id}")
    public ResponseEntity<StudentRecord> editStudent(@PathVariable Long id,
                                                     @RequestBody StudentRecord studentRecord) {
        StudentRecord foundStudent = studentService.editStudent(id, studentRecord);
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = "age")
    public ResponseEntity<Collection<StudentRecord>> findStudentsByAge(@RequestParam int age) {
        return ResponseEntity.ok(studentService.findByAge(age));
    }

    @GetMapping(params = {"min", "max"})
    public ResponseEntity<Collection<StudentRecord>> findStudentsByAgeBetween(@RequestParam("min") int min,
                                                                              @RequestParam("max") int max) {
        return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<FacultyRecord> getStudentFaculty(@PathVariable long id) {
        return ResponseEntity.ok(studentService.getStudentFaculty(id));
    }
}