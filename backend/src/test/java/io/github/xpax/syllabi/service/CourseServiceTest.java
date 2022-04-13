package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private InstituteRepository instituteRepository;
    @Mock
    private SemesterRepository semesterRepository;

    private CourseService courseService;

    private Institute organizer;
    private Institute organizer2;
    private Page<CourseForPage> coursePage;
    private CourseDetails course;
    private Course algorithms;
    private Course computation;
    private Course courseWithId3;
    private Program program;
    private Semester semester;
    private NewCourseRequest request;
    private UpdateCourseRequest updateRequest;
    private CourseSummary courseMin;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();


    @BeforeEach
    void setUp() {

        organizer = Institute.builder()
                .id(2)
                .name("Institute of Philosophy")
                .build();
        organizer2 = Institute.builder()
                .id(0)
                .name("Institute of Computer Science")
                .build();

        coursePage = Page.empty();
        courseWithId3 = Course.builder()
                .id(3)
                .name("Artificial Intelligence")
                .build();

        this.course = factory.createProjection(CourseDetails.class, courseWithId3);
        this.courseMin = factory.createProjection(CourseSummary.class, courseWithId3);

        program = Program.builder()
                .id(0)
                .name("Computer Science")
                .build();
        semester = Semester.builder()
                .id(0)
                .build();
        algorithms = Course.builder()
                .id(1)
                .name("Algorithms and Data Structures")
                .build();
        computation = Course.builder()
                .id(2)
                .name("Theory of Computation")
                .build();

        request = new NewCourseRequest();
        request.setName("Introduction to Epistemology");
        request.setEcts(2);
        request.setFacultative(false);
        request.setOrganizerId(2);

        updateRequest = new UpdateCourseRequest();
        List<Integer> prerequisites = new ArrayList<>();
        prerequisites.add(1);
        prerequisites.add(2);
        updateRequest.setPrerequisites(prerequisites);
        List<Integer> programs = new ArrayList<>();
        programs.add(0);
        updateRequest.setSemesters(programs);
        updateRequest.setName("Artificial Intelligence");
        updateRequest.setOrganizerId(0);
    }

    private void injectMocks() {
        courseService = new CourseService(courseRepository, instituteRepository, semesterRepository);
    }

    @Test
    void shouldAskRepositoryForCourses() {
        given(courseRepository.findAllProjectedBy(any(PageRequest.class), any(Class.class)))
                .willReturn(coursePage);
        injectMocks();

        Page<CourseForPage> result = courseService.getAllCourses(0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);

        then(courseRepository)
                .should(times(1))
                .findAllProjectedBy(pageRequestCaptor.capture(), any(Class.class));
        PageRequest pageRequest = pageRequestCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());

        assertThat(result, is(sameInstance(coursePage)));
    }

    @Test
    void shouldReturnCourse() {
        given(courseRepository.findProjectedById(anyInt(), any(Class.class)))
                .willReturn(Optional.of(course));
        injectMocks();

        CourseDetails result = courseService.getCourse(3);

        assertNotNull(result);
        assertEquals("Artificial Intelligence", result.getName());
        assertEquals(3, result.getId());
    }

    @Test
    void shouldThrowExceptionIfCourseNotFound() {
        given(courseRepository.findProjectedById(anyInt(), any(Class.class)))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> courseService.getCourse(0));
    }

    @Test
    void shouldAddNewCourse() {
        given(instituteRepository.getOne(2))
                .willReturn(organizer);
        injectMocks();

        courseService.addNewCourse(request);

        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        then(courseRepository)
                .should(times(1))
                .save(courseCaptor.capture());
        Course addedCourse = courseCaptor.getValue();

        assertNotNull(addedCourse);
        assertNotNull(addedCourse.getOrganizer());
        assertEquals(2, addedCourse.getOrganizer().getId());
        assertEquals("Institute of Philosophy", addedCourse.getOrganizer().getName());
        assertEquals(2, addedCourse.getEcts());
        assertEquals("Introduction to Epistemology", addedCourse.getName());
        assertFalse(addedCourse.getFacultative());
    }

    @Test
    void shouldUpdateCourse() {
        given(instituteRepository.getOne(0))
                .willReturn(organizer2);
        given(courseRepository.getOne(1))
                .willReturn(algorithms);
        given(courseRepository.getOne(2))
                .willReturn(computation);
        given(courseRepository.findById(3))
                .willReturn(Optional.of(courseWithId3));
        given(semesterRepository.getOne(0))
                .willReturn(semester);
        injectMocks();

        courseService.updateCourse(updateRequest, 3);

        then(courseRepository)
                .should(times(2))
                .getOne(anyInt());
        then(instituteRepository)
                .should(times(1))
                .getOne(anyInt());

        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);

        then(courseRepository)
                .should(times(1))
                .save(courseArgumentCaptor.capture());
        Course updatedCourse = courseArgumentCaptor.getValue();

        assertNotNull(updatedCourse);
        assertThat(updatedCourse.getPrerequisites(), hasSize(2));
        assertThat(updatedCourse.getSemesters(), hasSize(1));
        assertEquals("Artificial Intelligence", updatedCourse.getName());
        assertEquals(3, updatedCourse.getId());
    }

    @Test
    void shouldDeleteCourse() {
        injectMocks();

        courseService.deleteCourse(5);

        then(courseRepository)
                .should(times(1))
                .deleteById(5);
    }

    @Test
    void shouldReturnCourseMin() {
        given(courseRepository.getProjectedById(anyInt(), any(Class.class)))
                .willReturn(Optional.of(courseMin));
        injectMocks();

        CourseSummary result = courseService.getCourseMin(3);

        assertNotNull(result);
        assertEquals("Artificial Intelligence", result.getName());
        assertEquals(3, result.getId());
    }

    @Test
    void shouldAskRepositoryForCoursesByProgramId() {
        given(courseRepository.findBySemestersProgramId(anyInt(), any(PageRequest.class)))
                .willReturn(coursePage);
        injectMocks();

        Page<CourseForPage> result = courseService.getAllCoursesByProgramId(0, 20, 15);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> programIdCaptor = ArgumentCaptor.forClass(Integer.class);

        then(courseRepository)
                .should(times(1))
                .findBySemestersProgramId(programIdCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer programId = programIdCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(15, programId);

        assertThat(result, is(sameInstance(coursePage)));
    }

    @Test
    void shouldAskRepositoryForCoursesByUserId() {
        given(courseRepository.findByUserId(anyInt(), any(PageRequest.class)))
                .willReturn(coursePage);
        injectMocks();

        Page<CourseForPage> result = courseService.getAllCoursesByUserId(0, 20, 7);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> userIdCaptor = ArgumentCaptor.forClass(Integer.class);

        then(courseRepository)
                .should(times(1))
                .findByUserId(userIdCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer userId = userIdCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(7, userId);

        assertThat(result, is(sameInstance(coursePage)));
    }

    @Test
    void shouldAskRepositoryForCoursesBySemesterId() {
        given(courseRepository.findBySemestersId(anyInt(), any(PageRequest.class)))
                .willReturn(coursePage);
        injectMocks();

        Page<CourseForPage> result = courseService.getAllCoursesBySemesterId(0, 20, 15);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> programIdCaptor = ArgumentCaptor.forClass(Integer.class);

        then(courseRepository)
                .should(times(1))
                .findBySemestersId(programIdCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer programId = programIdCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(15, programId);

        assertThat(result, is(sameInstance(coursePage)));
    }
}