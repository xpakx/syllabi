package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    void setUp() {
        courseLiteraturePage = Page.empty();
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
}
