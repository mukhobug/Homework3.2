package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.component.RecordMapper;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.repository.FacultyRepository;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    private final RecordMapper recordMapper;

    public FacultyService(FacultyRepository facultyRepository,
                          RecordMapper recordMapper) {
        this.facultyRepository = facultyRepository;
        this.recordMapper = recordMapper;
    }

    public FacultyRecord addFaculty(FacultyRecord facultyRecord) {
        return recordMapper.toRecord(facultyRepository.save(recordMapper.toEntity(facultyRecord)));
    }

    public FacultyRecord findFaculty(long id) {
        return recordMapper.toRecord(facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new));
    }

    public FacultyRecord editFaculty(long id, FacultyRecord facultyRecord) {
        Faculty temp = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        temp.setName(facultyRecord.getName());
        temp.setColor(facultyRecord.getColor());
        return recordMapper.toRecord(facultyRepository.save(temp));
    }

    public FacultyRecord deleteFaculty(long id) {
        Faculty faculty = facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new);
        facultyRepository.delete(faculty);
        return recordMapper.toRecord(faculty);
    }

    public Collection<FacultyRecord> findByColorIgnoreCaseOrNameIgnoreCase(String colorOrName) {
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getAllStudents(long id) {
        return facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new).getStudents().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }
}