package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.component.RecordMapper;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final RecordMapper recordMapper;

    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository, RecordMapper recordMapper) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        this.recordMapper = recordMapper;
    }

    public StudentRecord addStudent(StudentRecord studentRecord) {
        logger.info("was invoking method addStudent");
        return recordMapper.toRecord(studentRepository.save(recordMapper.toEntity(studentRecord)));
    }

    public StudentRecord getStudent(long id) {
        logger.info("was invoking method getStudent");
        return recordMapper.toRecord(studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is not student with id = " + id);
            throw new StudentNotFoundException();
        }));
    }

    public StudentRecord setStudent(StudentRecord studentRecord) {
        logger.info("was invoking method setStudent");

        Student temp = studentRepository.findById(studentRecord.getId()).orElseThrow(() -> {
            logger.error("There is not student with id = " + studentRecord.getId());
            throw new StudentNotFoundException();
        });

        temp.setName(studentRecord.getName());
        temp.setAge(studentRecord.getAge());
        temp.setFaculty(facultyRepository.findById(studentRecord.getFacultyRecord().getId())
                .orElseThrow(() -> {
                    logger.error("There is not faculty with id = " + studentRecord.getFacultyRecord().getId());
                    throw new FacultyNotFoundException();
                }));
        return recordMapper.toRecord(studentRepository.save(temp));
    }

    public void deleteStudent(long id) {
        logger.info("was invoking method deleteStudent");
        studentRepository.deleteById(id);
    }

    public Collection<StudentRecord> getStudentsByAge(int age) {
        logger.info("was invoking method getStudentsByAge");
        return studentRepository.findByAge(age).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> getStudentsByAgeBetween(int min, int max) {
        logger.info("was invoking method getStudentsByAgeBetween");
        return studentRepository.findByAgeBetween(min, max).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public FacultyRecord getFacultyByStudentId(Long id) {
        logger.info("was invoking method getFacultyByStudentId");
        return getStudent(id).getFacultyRecord();
    }

    public Integer getStudentCount() {
        logger.info("was invoking method getStudentCount");
        return studentRepository.getStudentsCount();
    }

    public Double getStudentsAvgAge() {
        logger.info("was invoking method getStudentsAvgAge");
        return studentRepository.getStudentsAverageAge();
    }

    public Collection<StudentRecord> getLastFiveStudents() {
        logger.info("was invoking method getLastFiveStudents");
        return studentRepository.findLastFiveStudents().stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }
}
