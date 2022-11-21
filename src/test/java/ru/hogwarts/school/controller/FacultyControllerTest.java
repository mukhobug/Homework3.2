package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.component.RecordMapper;
import ru.hogwarts.school.entity.Faculty;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.record.FacultyRecord;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FacultyController.class)
@ExtendWith(MockitoExtension.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private FacultyService facultyService;
    @MockBean
    private FacultyRepository facultyRepository;
    @SpyBean
    private RecordMapper recordMapper;

    @Test
    void addFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("name");
        faculty.setColor("color");

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setName("name");
        facultyRecord.setColor("color");

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facultyRecord)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    FacultyRecord facultyRecord1 = objectMapper.readValue(mockHttpServletResponse
                            .getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
                    assertThat(facultyRecord1).isNotNull()
                            .usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(facultyRecord);
                    assertThat(facultyRecord1.getId()).isEqualTo(faculty.getId());
                });
    }

    @Test
    void getFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("name");
        faculty.setColor("color");

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setName("name");
        facultyRecord.setColor("color");

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/" + faculty.getId()))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    FacultyRecord facultyRecord1 = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
                    assertThat(facultyRecord1).isNotNull()
                            .usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(facultyRecord);
                    assertThat(facultyRecord1.getId()).isEqualTo(faculty.getId());
                });

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatExceptionOfType(FacultyNotFoundException.class)
                .isThrownBy(() -> facultyService.getFaculty(faculty.getId()));
    }

    @Test
    void setFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("name");
        faculty.setColor("color");

        FacultyRecord facultyRecord = new FacultyRecord();
        facultyRecord.setId(1L);
        facultyRecord.setName("name");
        facultyRecord.setColor("color");

        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(faculty));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facultyRecord)))
                .andExpect(result -> {
                    MockHttpServletResponse mockHttpServletResponse = result.getResponse();
                    FacultyRecord facultyRecord1 = objectMapper.readValue(mockHttpServletResponse.getContentAsString(StandardCharsets.UTF_8), FacultyRecord.class);
                    assertThat(facultyRecord1).isNotNull()
                            .usingRecursiveComparison()
                            .ignoringFields("id")
                            .isEqualTo(facultyRecord);
                    assertThat(facultyRecord1.getId()).isEqualTo(faculty.getId());
                });
    }

    @Test
    void getFacultiesByColorOrName() {
    }

    @Test
    void getStudentsByFaculty() {
    }
}