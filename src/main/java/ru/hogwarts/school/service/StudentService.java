package ru.hogwarts.school.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.component.RecordMapper;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private final RecordMapper recordMapper;

    public StudentService(StudentRepository studentRepository,
                          RecordMapper recordMapper) {
        this.studentRepository = studentRepository;
        this.recordMapper = recordMapper;
    }

    public StudentRecord addStudent(StudentRecord studentRecord) {
        return recordMapper.toRecord(studentRepository.save(recordMapper.toEntity(studentRecord)));
    }

    public StudentRecord findStudent(long id) {
        return recordMapper.toRecord(studentRepository.findById(id).orElseThrow(StudentNotFoundException::new));
    }

    public StudentRecord editStudent(long id, StudentRecord studentRecord) {
        Student temp = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        temp.setName(studentRecord.getName());
        temp.setAge(studentRecord.getAge());
        return recordMapper.toRecord(studentRepository.save(temp));
    }

    public StudentRecord deleteStudent(long id) {
        Student student = studentRepository.findById(id).orElseThrow(StudentNotFoundException::new);
        studentRepository.delete(student);
        return recordMapper.toRecord(student);
    }

    public Collection<StudentRecord> findByAge(int age) {
        return studentRepository.findByAge(age).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public Collection<StudentRecord> findByAgeBetween(int min, int max) {
        return studentRepository.findByAgeBetween(min, max).stream()
                .map(recordMapper::toRecord)
                .collect(Collectors.toList());
    }

    public FacultyRecord getStudentFaculty(Long id) {
        return findStudent(id).getFaculty();
    }
}