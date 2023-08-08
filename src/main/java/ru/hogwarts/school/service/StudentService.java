package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
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

    public Collection<String> getStudentsStartsWith(String letter) {
        logger.info("was invoking method getStudentsStartsWith");
        return studentRepository.findAll().stream()
                .parallel()
                .filter(s -> s.getName().startsWith(letter))
                .map(s -> s.getName().toUpperCase())
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getStudentsAverageAgeByStream() {
        logger.info("was invoking method getStudentsAverageAgeByStream");
        return studentRepository.findAll().stream()
                .parallel()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0);
    }

    public void multiThreadMethod() {
        logger.debug("was invoking method multiThreadMethodNameList");
        List<Student> list = studentRepository.findAll(PageRequest.of(0, 6)).getContent();

        logger.info(list.get(0).getName());
        logger.info(list.get(1).getName());

        new Thread(() -> {
            logger.info(list.get(2).getName());
            logger.info(list.get(3).getName());
        }).start();

        new Thread(() -> {
            logger.info(list.get(4).getName());
            logger.info(list.get(5).getName());
        }).start();
    }

    public void multiThreadMethodSync() {
        logger.debug("was invoking method multiThreadMethodNameListSync");
        List<Student> list = studentRepository.findAll(PageRequest.of(0, 6)).getContent();

        printNames(list.subList(0, 2));
        new Thread(() -> printNames(list.subList(2, 4))).start();
        new Thread(() -> printNames(list.subList(4, 6))).start();
    }

    private synchronized void printNames(List<Student> students) {
        students.forEach(s -> logger.info(s.getName()));
    }
}
