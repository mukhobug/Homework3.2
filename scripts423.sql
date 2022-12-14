SELECT s.name as name, s.age as age, f.name as faculty
FROM student s
         INNER JOIN faculty f on f.id = s.faculty_id;

SELECT s.name as name, s.age as age
from student s
         INNER JOIN avatar a on s.id = a.student_id;