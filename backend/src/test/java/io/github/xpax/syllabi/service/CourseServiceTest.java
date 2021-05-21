package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.CourseDetails;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseRepository;
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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
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

    private CourseService courseService;

    private Institute organizer;
    private Institute organizer2;
    private Page<CourseForPage> coursePage;
    private CourseDetails course;
    private Course algorithms;
    private Course computation;
    private Program program;

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
        Course course = Course.builder()
                .id(3)
                .name("Artificial Intelligence")
                .build();

        this.course = factory.createProjection(CourseDetails.class, course);

        program = Program.builder()
                .id(0)
                .name("Computer Science")
                .build();
        algorithms = Course.builder()
                .id(1)
                .name("Algorithms and Data Structures")
                .build();
        computation = Course.builder()
                .id(2)
                .name("Theory of Computation")
                .build();

    }

    private void injectMocks() {
        courseService = new CourseService(courseRepository);
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


}