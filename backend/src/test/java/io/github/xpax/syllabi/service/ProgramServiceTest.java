package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.ProgramDetails;
import io.github.xpax.syllabi.entity.dto.ProgramRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.ProgramRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProgramServiceTest {
    @Mock
    private CourseRepository courseRepository;
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
        List<Integer> courses = new ArrayList<>();
        courses.add(1);
        courses.add(2);
        courses.add(3);
        request.setCoursesId(courses);
        request.setOrganizerId(9);
        page = Page.empty();
    }

    private void injectMocks() {
        programService = new ProgramService(courseRepository, programRepository, instituteRepository);
    }

    @Test
    void shouldAddNewProgram() {
        given(courseRepository.getOne(1))
                .willReturn(course1);
        given(courseRepository.getOne(2))
                .willReturn(course2);
        given(courseRepository.getOne(3))
                .willReturn(course3);
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

        assertNotNull(addedProgram.getCourses());
        assertThat(addedProgram.getCourses(), hasSize(3));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("id", equalTo(1))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("id", equalTo(2))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("id", equalTo(3))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("name", equalTo("Ethics"))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("name", equalTo("Epistemology"))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("name", equalTo("Metaphysics"))));

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
        given(courseRepository.getOne(1))
                .willReturn(course1);
        given(courseRepository.getOne(2))
                .willReturn(course2);
        given(courseRepository.getOne(3))
                .willReturn(course3);
        given(instituteRepository.getOne(9))
                .willReturn(organizer);
        injectMocks();

        programService.updateProgram(request, 3);

        ArgumentCaptor<Program> programCaptor = ArgumentCaptor.forClass(Program.class);
        then(programRepository)
                .should(times(1))
                .save(programCaptor.capture());
        Program addedProgram = programCaptor.getValue();

        assertNotNull(addedProgram);
        assertNotNull(addedProgram.getOrganizer());
        assertEquals(9, addedProgram.getOrganizer().getId());
        assertEquals("Institute of Philosophy", addedProgram.getOrganizer().getName());

        assertNotNull(addedProgram.getCourses());
        assertThat(addedProgram.getCourses(), hasSize(3));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("id", equalTo(1))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("id", equalTo(2))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("id", equalTo(3))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("name", equalTo("Ethics"))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("name", equalTo("Epistemology"))));
        assertThat(addedProgram.getCourses(), hasItem(hasProperty("name", equalTo("Metaphysics"))));

        assertEquals("Philosophy", addedProgram.getName());
        assertNotNull(addedProgram.getId());
        assertEquals(3, addedProgram.getId());
    }
}
