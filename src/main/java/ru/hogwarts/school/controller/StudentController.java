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

    @PostMapping
    public StudentRecord addStudent(@RequestBody StudentRecord studentRecord) {
        return studentService.addStudent(studentRecord);
    }

    @PutMapping
    public ResponseEntity<StudentRecord> setStudent(@RequestBody StudentRecord studentRecord) {
        StudentRecord foundStudent = studentService.setStudent(studentRecord);
        return ResponseEntity.ok(foundStudent);
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentRecord> getStudent(@PathVariable Long id) {
        StudentRecord studentRecord = studentService.getStudent(id);
        return ResponseEntity.ok(studentRecord);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = "age")
    public ResponseEntity<Collection<StudentRecord>> getStudentsByAge(@RequestParam int age) {
        return ResponseEntity.ok(studentService.getStudentsByAge(age));
    }

    @GetMapping(params = {"min", "max"})
    public ResponseEntity<Collection<StudentRecord>> getStudentsByAgeBetween(@RequestParam("min") int min,
                                                                             @RequestParam("max") int max) {
        return ResponseEntity.ok(studentService.getStudentsByAgeBetween(min, max));
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<FacultyRecord> getFacultyByStudentId(@PathVariable long id) {
        return ResponseEntity.ok(studentService.getFacultyByStudentId(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> getStudentCount() {
        return ResponseEntity.ok(studentService.getStudentCount());
    }

    @GetMapping("/average-age")
    public ResponseEntity<Double> getStudentsAvgAge() {
        return ResponseEntity.ok(studentService.getStudentsAvgAge());
    }

    @GetMapping("/last-five")
    public ResponseEntity<Collection<StudentRecord>> getLastFiveStudents() {
        return ResponseEntity.ok(studentService.getLastFiveStudents());
    }

    @GetMapping("/starts-with")
    public ResponseEntity<Collection<String>> getStudentsStartsWith(@RequestParam String letter) {
        return ResponseEntity.ok(studentService.getStudentsStartsWith(letter));
    }

    @GetMapping("/average-age-by-stream")
    public ResponseEntity<Double> getStudentsAverageAgeByStream() {
        return ResponseEntity.ok(studentService.getStudentsAverageAgeByStream());
    }

    @GetMapping("/multi-thread-endpoint-name-list")
    public ResponseEntity<Void> printNames() {
        studentService.multiThreadMethod();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/multi-thread-endpoint-name-list-sync")
    public ResponseEntity<Void> printNames2() {
        studentService.multiThreadMethodSync();
        return ResponseEntity.ok().build();
    }
}
