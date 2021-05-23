package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.entity.dto.LiteratureRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseLiteratureRepository;
import io.github.xpax.syllabi.repo.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
class CourseLiteratureServiceTest {
    @Mock
    private CourseLiteratureRepository courseLiteratureRepository;
    @Mock
    private CourseRepository courseRepository;

    private CourseLiteratureService courseLiteratureService;

    private Page<LiteratureForPage> courseLiteraturePage;
    private CourseLiterature literature;
    private Course course;
    private LiteratureRequest request;
    private LiteratureRequest editRequest;
    private CourseLiterature literatureBeforeEdit;

    @BeforeEach
    void setUp() {
        courseLiteraturePage = Page.empty();

        literature = CourseLiterature.builder()
                .id(0)
                .author("Dante Alighieri")
                .title("Divine Comedy")
                .build();
        course = Course.builder()
                .id(1)
                .name("Argumentation Theory")
                .build();
        request = new LiteratureRequest();
        request.setAuthor("Stephen Toulmin");
        request.setTitle("The Uses of Argument");
        editRequest = new LiteratureRequest();
        editRequest.setObligatory(true);
        editRequest.setAuthor("Sarah Blaffer Hrdy");
        editRequest.setTitle("The Woman That Never Evolved");
        Course secondCourse = Course.builder()
                .id(5)
                .name("Sociobiology")
                .build();
        literatureBeforeEdit = CourseLiterature.builder()
                .id(2)
                .author("Sarah Hrdy")
                .title("The Woman That Never Evolved")
                .course(secondCourse)
                .obligatory(false)
                .build();
    }

    private void injectMocks() {
        courseLiteratureService = new CourseLiteratureService(courseLiteratureRepository, courseRepository);
    }

    @Test
    void shouldAskRepositoryForPageOfLiterature() {
        given(courseLiteratureRepository.findAllByCourseId(anyInt(), any(PageRequest.class)))
                .willReturn(courseLiteraturePage);
        injectMocks();

        Page<LiteratureForPage> result = courseLiteratureService.getAllLiterature(0, 20, 0);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> courseIdCaptor = ArgumentCaptor.forClass(Integer.class);

        then(courseLiteratureRepository)
                .should(times(1))
                .findAllByCourseId(courseIdCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer courseId = courseIdCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(0, courseId);

        assertThat(result, is(sameInstance(courseLiteraturePage)));
    }

    @Test
    void shouldDeleteLiterature() {
        injectMocks();
        courseLiteratureService.deleteLiterature(0);

        then(courseLiteratureRepository)
                .should(times(1))
                .deleteById(0);
    }

    @Test
    void shouldReturnLiteratureIfFound() {
        given(courseLiteratureRepository.findById(0))
                .willReturn(Optional.of(literature));
        injectMocks();

        CourseLiterature result = courseLiteratureService.getLiterature(0);

        assertNotNull(result);
        assertEquals("Dante Alighieri", result.getAuthor());
        assertEquals("Divine Comedy", result.getTitle());
        assertEquals(0, result.getId());
    }

    @Test
    void shouldThrowExceptionIfLiteratureNotFound() {
        given(courseLiteratureRepository.findById(0))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> courseLiteratureService.getLiterature(0));
    }

    @Test
    void shouldAddNewLiteratureToCourse() {
        given(courseRepository.getOne(1))
                .willReturn(course);
        injectMocks();

        courseLiteratureService.addNewLiterature(request, 1);

        ArgumentCaptor<CourseLiterature> literatureCaptor = ArgumentCaptor.forClass(CourseLiterature.class);
        then(courseLiteratureRepository)
                .should(times(1))
                .save(literatureCaptor.capture());
        CourseLiterature courseLiterature = literatureCaptor.getValue();

        assertNotNull(courseLiterature);
        assertEquals("Stephen Toulmin", courseLiterature.getAuthor());
        assertEquals("The Uses of Argument", courseLiterature.getTitle());
        assertNotNull(courseLiterature.getCourse());
        assertEquals(1, courseLiterature.getCourse().getId());
    }

    @Test
    void shouldUpdateLiterature() {
        given(courseLiteratureRepository.findById(2))
                .willReturn(Optional.of(literatureBeforeEdit));
        injectMocks();

        courseLiteratureService.updateLiterature(editRequest, 2);

        ArgumentCaptor<CourseLiterature> literatureCaptor = ArgumentCaptor.forClass(CourseLiterature.class);
        then(courseLiteratureRepository)
                .should(times(1))
                .save(literatureCaptor.capture());
        CourseLiterature courseLiterature = literatureCaptor.getValue();

        assertNotNull(courseLiterature);
        assertEquals("Sarah Blaffer Hrdy", courseLiterature.getAuthor());
        assertEquals("The Woman That Never Evolved", courseLiterature.getTitle());
        assertTrue(courseLiterature.getObligatory());
    }
}
