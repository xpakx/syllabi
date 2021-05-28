package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.dto.ProgramDetails;
import io.github.xpax.syllabi.entity.dto.ProgramRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.ProgramRepository;
import io.github.xpax.syllabi.repo.SemesterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {
    @Mock
    private SemesterRepository semesterRepository;
    @Mock
    private ProgramRepository programRepository;
    @Mock
    private InstituteRepository instituteRepository;
    private ProgramService programService;
    private Program program;
    private ProgramRequest request;
    private Institute organizer;
    private Course course1;
    private Course course2;
    private Course course3;
    private ProgramDetails programDetails;

    private Page<Program> page;
    private Page<Semester> semesterPage;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        program = Program.builder()
                .name("Computer Science")
                .id(1)
                .build();
        this.programDetails = factory.createProjection(ProgramDetails.class, program);
        course1 = Course.builder()
                .id(1)
                .name("Epistemology")
                .build();
        course2 = Course.builder()
                .id(2)
                .name("Ethics")
                .build();
        course3 = Course.builder()
                .id(3)
                .name("Metaphysics")
                .build();
        organizer = Institute.builder()
                .id(9)
                .name("Institute of Philosophy")
                .build();
        request = new ProgramRequest();
        request.setName("Philosophy");
        request.setOrganizerId(9);
        page = Page.empty();
        semesterPage = Page.empty();
    }

    private void injectMocks() {
        programService = new ProgramService(programRepository, instituteRepository, semesterRepository);
    }

    @Test
    void shouldAddNewProgram() {
        given(instituteRepository.getOne(9))
                .willReturn(organizer);
        injectMocks();

        programService.addNewProgram(request);

        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);
        then(programRepository)
                .should(times(1))
                .save(programCaptor.capture());
        Program addedProgram = programCaptor.getValue();

        assertNotNull(addedProgram);
        assertNotNull(addedProgram.getOrganizer());
        assertEquals(9, addedProgram.getOrganizer().getId());
        assertEquals("Institute of Philosophy", addedProgram.getOrganizer().getName());

        assertEquals("Philosophy", addedProgram.getName());
        assertNull(addedProgram.getId());
    }

    @Test
    void shouldDeleteProgram() {
        injectMocks();
        programService.deleteProgram(3);
        then(programRepository)
                .should(times(1))
                .deleteById(3);
    }

    @Test
    void shouldReturnProgram() {
        given(programRepository.findProjectedById(1))
                .willReturn(Optional.of(programDetails));
        injectMocks();
        ProgramDetails result = programService.getProgram(1);

        assertNotNull(result);
        assertEquals("Computer Science", result.getName());
        assertEquals(1, result.getId());
    }

    @Test
    void shouldThrowExceptionIfProgramNotFound() {
        given(programRepository.findProjectedById(anyInt()))
                .willReturn(Optional.empty());
        injectMocks();
        assertThrows(NotFoundException.class, () -> programService.getProgram(1));
    }

    @Test
    void shouldUpdateProgram() {
        given(instituteRepository.getOne(9))
                .willReturn(organizer);
        given(programRepository.findById(1))
                .willReturn(Optional.of(program));
        injectMocks();

        programService.updateProgram(request, 1);

        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);
        then(programRepository)
                .should(times(1))
                .save(programCaptor.capture());
        Program addedProgram = programCaptor.getValue();

        assertNotNull(addedProgram);
        assertNotNull(addedProgram.getOrganizer());
        assertEquals(9, addedProgram.getOrganizer().getId());
        assertEquals("Institute of Philosophy", addedProgram.getOrganizer().getName());

        assertEquals("Philosophy", addedProgram.getName());
        assertNotNull(addedProgram.getId());
        assertEquals(1, addedProgram.getId());
    }

    @Test
    void shouldAskRepositoryForPrograms() {
        given(programRepository.findAll(any(PageRequest.class)))
                .willReturn(page);
        injectMocks();

        Page<Program> result = programService.getAllPrograms(0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);

        then(programRepository)
                .should(times(1))
                .findAll(pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());

        assertThat(result, is(sameInstance(page)));
    }

    @Test
    void shouldAskRepositoryForSemesters() {
        given(semesterRepository.findByProgramId(anyInt(), any(PageRequest.class)))
                .willReturn(semesterPage);
        injectMocks();

        Page<Semester> result = programService.getAllSemesters(1, 0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);

        then(semesterRepository)
                .should(times(1))
                .findByProgramId(anyInt(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());

        assertThat(result, is(sameInstance(semesterPage)));
    }
}
