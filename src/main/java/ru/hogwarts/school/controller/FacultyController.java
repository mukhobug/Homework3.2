package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    public FacultyRecord addFaculty(@RequestBody FacultyRecord facultyRecord) {
        return facultyService.addFaculty(facultyRecord);
    }

    @GetMapping("{id}")
    public ResponseEntity<FacultyRecord> getFaculty(@PathVariable Long id) {
        FacultyRecord facultyRecord = facultyService.getFaculty(id);
        return ResponseEntity.ok(facultyRecord);
    }

    @PutMapping
    public ResponseEntity<FacultyRecord> setFaculty(@RequestBody FacultyRecord facultyRecord) {
        return ResponseEntity.ok(facultyService.setFaculty(facultyRecord));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<FacultyRecord> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = "colorOrName")
    public ResponseEntity<Collection<FacultyRecord>> getFacultiesByColorOrName(@RequestParam String colorOrName) {
        return ResponseEntity.ok(facultyService.getByColorOrName(colorOrName));
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<Collection<StudentRecord>> getStudentsByFaculty(@PathVariable long id) {
        return ResponseEntity.ok(facultyService.getStudentsByFacultyId(id));
    }

    @GetMapping("/max-length-name")
    public ResponseEntity<String> getMaxLengthName() {
        return ResponseEntity.ok(facultyService.getMaxLengthName());
    }

    @GetMapping("/int-value")
    public ResponseEntity<Integer> getIntValue() {
        return ResponseEntity.ok(facultyService.getIntValue());
    }
}