package ru.hogwarts.school.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(FacultyNotFoundException.class)
    public ResponseEntity<String> handleFacultyNotExistException() {
        return ResponseEntity.notFound().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotExistException() {
        return ResponseEntity.notFound().build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(AvatarNotFoundException.class)
    public ResponseEntity<String> handleAvatarNotExistException() {
        return ResponseEntity.notFound().build();
    }
}
