package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.entity.Student;

import java.util.Collection;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Collection<Student> findByAge(int age);

    Collection<Student> findByAgeBetween(int min, int max);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getStudentsCount();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double getStudentsAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    Collection<Student> findLastFiveStudents();
}
