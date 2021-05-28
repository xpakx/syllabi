package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.SemesterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class SemesterServiceTest {
    @Mock
    private SemesterRepository semesterRepository;

    private SemesterService semesterService;

    private Semester semester;

    @BeforeEach
    void setUp() {
        semester = Semester.builder()
                .name("Semester")
                .id(5)
                .number(1)
                .build();
    }

    private void injectMocks() {
        semesterService = new SemesterService(semesterRepository);
    }

    @Test
    void shouldReturnSemester() {
        given(semesterRepository.findById(5))
                .willReturn(Optional.of(semester));
        injectMocks();

        Semester result = semesterService.getSemester(5);

        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals(1, result.getNumber());
        assertEquals("Semester", result.getName());
    }

    @Test
    void shouldThrowExceptionIfSemesterNotFound() {
        given(semesterRepository.findById(anyInt()))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> semesterService.getSemester(5));
    }

    @Test
    void shouldDeleteSemester() {
        injectMocks();
        semesterService.deleteSemester(5);
        then(semesterRepository)
                .should(times(1))
                .deleteById(5);
    }
}
