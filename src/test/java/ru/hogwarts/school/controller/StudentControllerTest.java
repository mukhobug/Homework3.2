package ru.hogwarts.school.controller;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.record.StudentRecord;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyRepository facultyRepository;

    private final Faker faker = new Faker();

    @AfterEach
    public void afterEach() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    public void addStudentTest() {
        addStudent(generateStudent(addFaculty(generateFaculty())));
    }

    @Test
    public void getStudentTest() {
        FacultyRecord facultyRecord = addFaculty(generateFaculty());
        StudentRecord expected = addStudent(generateStudent(facultyRecord));

        ResponseEntity<StudentRecord> studentRecordResponseEntity =
                testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + expected.getId(),
                        StudentRecord.class);

        assertThat(studentRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRecordResponseEntity.getBody())
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void setStudentTest() {
        FacultyRecord facultyRecord1 = addFaculty(generateFaculty());
        FacultyRecord facultyRecord2 = addFaculty(generateFaculty());
        StudentRecord studentRecord = addStudent(generateStudent(facultyRecord1));

        StudentRecord expected = new StudentRecord();
        expected.setId(studentRecord.getId());
        expected.setAge(15);
        expected.setName("Name");
        expected.setFacultyRecord(addFaculty(facultyRecord2));

        ResponseEntity<StudentRecord> studentRecordResponseEntity =
                testRestTemplate.exchange("http://localhost:" + port + "/student/",
                        HttpMethod.PUT,
                        new HttpEntity<>(expected),
                        StudentRecord.class);

        assertThat(studentRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRecordResponseEntity.getBody())
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void deleteStudentTest() {
        FacultyRecord facultyRecord = addFaculty(generateFaculty());
        StudentRecord studentRecord = addStudent(generateStudent(facultyRecord));

        testRestTemplate.delete("http://localhost:" + port + "/student/" + studentRecord.getId());

        ResponseEntity<StudentRecord> studentRecordResponseEntity =
                testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + studentRecord.getId(),
                        StudentRecord.class);

        assertThat(studentRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void getStudentsByAgeTest() {
        FacultyRecord facultyRecord = addFaculty(generateFaculty());
        List<StudentRecord> studentRecords = Stream.generate(() -> generateStudent(facultyRecord))
                .limit(10)
                .map(this::addStudent)
                .collect(Collectors.toList());

        int age = faker.random().nextInt(11, 18);

        List<StudentRecord> expected = studentRecords.stream()
                .filter(s -> s.getAge() == age)
                .collect(Collectors.toList());

        ResponseEntity<Collection<StudentRecord>> collectionResponseEntity =
                testRestTemplate.exchange("http://localhost:" + port + "/student?age=" + age,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<>() {
                        });

        assertThat(collectionResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(collectionResponseEntity.getBody())
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void getStudentsByAgeBetweenTest() {
        List<FacultyRecord> facultyRecords = Stream.generate(this::generateFaculty)
                .limit(4)
                .map(this::addFaculty)
                .collect(Collectors.toList());
        List<StudentRecord> studentRecords = Stream.generate(() -> generateStudent(facultyRecords.get(faker.random().nextInt(facultyRecords.size()))))
                .limit(50)
                .map(this::addStudent)
                .collect(Collectors.toList());

        int min = faker.random().nextInt(11, 17);
        int max = faker.random().nextInt(min, 18);

        List<StudentRecord> expected = studentRecords.stream()
                .filter(s -> s.getAge() >= min && s.getAge() <= max)
                .collect(Collectors.toList());

        ResponseEntity<Collection<StudentRecord>> collectionResponseEntity =
                testRestTemplate.exchange("http://localhost:" + port + "/student/?min=" + min + "&max=" + max,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<>() {
                        });

        assertThat(collectionResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(collectionResponseEntity.getBody())
                .isNotNull()
                .hasSize(expected.size())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    public void getStudentFacultyTest() {
        FacultyRecord facultyRecord = addFaculty(generateFaculty());
        StudentRecord studentRecord = addStudent(generateStudent(facultyRecord));

        ResponseEntity<FacultyRecord> studentRecordResponseEntity =
                testRestTemplate.getForEntity("http://localhost:" + port + "/student/" + studentRecord.getId() + "/faculty",
                        FacultyRecord.class);

        assertThat(studentRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRecordResponseEntity.getBody())
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(facultyRecord);
    }

    private StudentRecord addStudent(StudentRecord studentRecord) {
        ResponseEntity<StudentRecord> studentRecordResponseEntity =
                testRestTemplate.postForEntity("http://localhost:" + port + "/student", studentRecord, StudentRecord.class);
        assertThat(studentRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(studentRecordResponseEntity.getBody())
                .isNotNull()
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(studentRecord);
        assertThat(studentRecordResponseEntity.getBody().getId()).isNotNull();

        return studentRecordResponseEntity.getBody();
    }

    private FacultyRecord addFaculty(FacultyRecord facultyRecord) {
        ResponseEntity<FacultyRecord> facultyRecordResponseEntity =
                testRestTemplate.postForEntity("http://localhost:" + port + "/faculty", facultyRecord, FacultyRecord.class);
        assertThat(facultyRecordResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(facultyRecordResponseEntity.getBody())
                .isNotNull()
                .usingRecursiveComparison().ignoringFields("id").isEqualTo(facultyRecord);
        assertThat(facultyRecordResponseEntity.getBody().getId()).isNotNull();

        return facultyRecordResponseEntity.getBody();
    }

    private StudentRecord generateStudent(FacultyRecord facultyRecord) {
        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setName(faker.harryPotter().character());
        if (facultyRecord != null) {
            studentRecord.setFacultyRecord(facultyRecord);
        }
        studentRecord.setAge(faker.random().nextInt(11, 18));
        return studentRecord;
    }

    private FacultyRecord generateFaculty() {
        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setName(faker.harryPotter().house());
        facultyRecord.setColor(faker.color().toString());
        return facultyRecord;
    }
}