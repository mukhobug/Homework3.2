package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository,
                          RecordMapper recordMapper) {
        this.facultyRepository = facultyRepository;
        this.recordMapper = recordMapper;
    }

    public FacultyRecord addFaculty(FacultyRecord facultyRecord) {
        logger.info("was invoking method addFaculty");
        return recordMapper.toRecord(facultyRepository.save(recordMapper.toEntity(facultyRecord)));
    }

    public FacultyRecord getFaculty(long id) {
        logger.info("was invoking method getFaculty");
        return recordMapper.toRecord(facultyRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + id);
            throw new FacultyNotFoundException();
        }));
    }

    public FacultyRecord setFaculty(FacultyRecord facultyRecord) {
        logger.info("was invoking method setFaculty");
        Faculty temp = facultyRepository.findById(facultyRecord.getId()).orElseThrow(() -> {
            logger.error("There is not faculty with id = " + facultyRecord.getId());
            throw new FacultyNotFoundException();
        });

        temp.setName(facultyRecord.getName());
        temp.setColor(facultyRecord.getColor());
        return recordMapper.toRecord(facultyRepository.save(temp));
    }

    public void deleteFaculty(long id) {
        logger.info("was invoking method deleteFaculty");
        facultyRepository.deleteById(id);
    }

    public Collection<FacultyRecord> getByColorOrName(String colorOrName) {
        logger.info("was invoking method getByColorOrName");
        return facultyRepository.findByColorIgnoreCaseOrNameIgnoreCase(colorOrName, colorOrName).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getStudentsByFacultyId(long id) {
        logger.info("was invoking method getStudentsByFacultyId");
        return facultyRepository.findById(id).orElseThrow(FacultyNotFoundException::new).getStudents().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public String getMaxLengthName() {
        logger.info("was invoking method getMaxLengthName");
        return facultyRepository.findAll().stream()
                .parallel()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length))
                .orElse("");
    }

    public Integer getIntValue() {
        logger.info("was invoking method getIntValue");
        return Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .parallel()
                .reduce(0, Integer::sum);
    }
}