package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.dto.CourseYearForPage;
import io.github.xpax.syllabi.entity.dto.CourseYearRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.CourseYearRepository;
import io.github.xpax.syllabi.repo.TeacherRepository;
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
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CourseYearServiceTest {

    @Mock
    private CourseYearRepository courseYearRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private TeacherRepository teacherRepository;

    private CourseYearService courseYearService;
    private CourseYearRequest request;
    private Course course;
    private Teacher firstTeacher;
    private Teacher secondTeacher;
    private Page<CourseYearForPage> courseYearPage;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        course = Course.builder()
                .id(17)
                .name("Introduction to Cognitive Science")
                .build();
        firstTeacher = Teacher.builder()
                .id(0)
                .name("John")
                .build();
        secondTeacher = Teacher.builder()
                .id(4)
                .name("Amy")
                .build();
        request = new CourseYearRequest();
        List<Integer> coordinators = new ArrayList<>();
        coordinators.add(0);
        coordinators.add(4);
        request.setCoordinators(coordinators);

        courseYearPage = Page.empty();
    }

    private void injectMocks() {
        courseYearService = new CourseYearService(courseRepository, courseYearRepository, teacherRepository);
    }

    @Test
    void shouldAddNewCourseYear() {
        given(teacherRepository.getOne(0))
                .willReturn(firstTeacher);
        given(teacherRepository.getOne(0))
                .willReturn(secondTeacher);
        given(courseRepository.getOne(17))
                .willReturn(course);
        injectMocks();

        courseYearService.addNewCourseYear(17, request);
        ArgumentCaptor<CourseYear> yearArgumentCaptor = ArgumentCaptor.forClass(CourseYear.class);
        then(courseYearRepository)
                .should(times(1))
                .save(yearArgumentCaptor.capture());
        CourseYear year = yearArgumentCaptor.getValue();

        assertNotNull(year);
        assertThat(year.getCoordinatedBy(), hasSize(2));
        assertNotNull(year.getParent());
        assertEquals(17, year.getParent().getId());
        assertNull(year.getId());
    }

    @Test
    void shouldAskRepositoryForCourseYears() {
        given(courseYearRepository.findAllByParentId(anyInt(), any(PageRequest.class)))
                .willReturn(courseYearPage);
        injectMocks();

        Page<CourseYearForPage> result = courseYearService.getYearsForCourse(3, 0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);

        then(courseYearRepository)
                .should(times(1))
                .findAllByParentId(integerCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer courseId = integerCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(3, courseId);

        assertThat(result, is(sameInstance(courseYearPage)));
    }

    @Test
    void shouldAskRepositoryForActiveCourseYears() {
        given(courseYearRepository.findByParentIdAndEndDateAfter(anyInt(), any(Date.class), any(PageRequest.class)))
                .willReturn(courseYearPage);
        injectMocks();

        Page<CourseYearForPage> result = courseYearService.getActiveYearsForCourse(3, 0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);

        then(courseYearRepository)
                .should(times(1))
                .findByParentIdAndEndDateAfter(integerCaptor.capture(), any(Date.class), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer courseId = integerCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(3, courseId);

        assertThat(result, is(sameInstance(courseYearPage)));
    }
}
