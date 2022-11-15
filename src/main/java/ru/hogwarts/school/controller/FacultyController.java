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

    @GetMapping("{id}")
    public ResponseEntity<FacultyRecord> getFacultyInfo(@PathVariable Long id) {
        FacultyRecord facultyRecord = facultyService.findFaculty(id);
        return ResponseEntity.ok(facultyRecord);
    }

    @PostMapping
    public FacultyRecord createFaculty(@RequestBody FacultyRecord facultyRecord) {
        return facultyService.addFaculty(facultyRecord);
    }

    @PutMapping("{id}")
    public ResponseEntity<FacultyRecord> editFaculty(@PathVariable Long id,
                                                     @RequestBody FacultyRecord facultyRecord) {
        FacultyRecord foundFaculty = facultyService.editFaculty(id, facultyRecord);
        return ResponseEntity.ok(foundFaculty);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<FacultyRecord> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(params = "colorOrName")
    public ResponseEntity<Collection<FacultyRecord>> findFaculties(@RequestParam String colorOrName) {
        return ResponseEntity.ok(facultyService.findByColorOrNameIgnoreCase(colorOrName));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Collection<StudentRecord>> findStudents(@PathVariable long id) {
        return ResponseEntity.ok(facultyService.getAllStudents(id));
    }
}