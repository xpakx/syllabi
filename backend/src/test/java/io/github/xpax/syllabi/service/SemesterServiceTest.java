package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.SemesterRequest;
import io.github.xpax.syllabi.entity.dto.SemesterSummary;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.SemesterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    private SemesterSummary semesterSum;
    private SemesterRequest semesterRequest;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        semester = Semester.builder()
                .name("Semester")
                .id(5)
                .number(1)
                .build();
        semesterSum = factory.createProjection(SemesterSummary.class, semester);
        semesterRequest = new SemesterRequest();
        semesterRequest.setName("Semester");
        semesterRequest.setNumber(1);
    }

    private void injectMocks() {
        semesterService = new SemesterService(semesterRepository);
    }

    @Test
    void shouldReturnSemester() {
        given(semesterRepository.findProjectedById(5))
                .willReturn(Optional.of(semesterSum));
        injectMocks();

        SemesterSummary result = semesterService.getSemester(5);

        assertNotNull(result);
        assertEquals(5, result.getId());
        assertEquals(1, result.getNumber());
        assertEquals("Semester", result.getName());
    }

    @Test
    void shouldThrowExceptionIfSemesterNotFound() {
        given(semesterRepository.findProjectedById(anyInt()))
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

    @Test
    void shouldUpdateSemester() {
        given(semesterRepository.findById(5))
                .willReturn(Optional.of(semester));
        injectMocks();

        semesterService.updateSemester(semesterRequest, 5);

        ArgumentCaptor<Semester> groupCaptor = ArgumentCaptor.forClass(Semester.class);
        then(semesterRepository)
                .should(times(1))
                .save(groupCaptor.capture());
        Semester updatedSemester = groupCaptor.getValue();

        assertNotNull(updatedSemester);

        assertNotNull(updatedSemester.getId());
        assertEquals(5, updatedSemester.getId());
        assertEquals(1, updatedSemester.getNumber());
        assertEquals("Semester", updatedSemester.getName());
    }
}
