package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.CourseType;
import io.github.xpax.syllabi.entity.dto.CourseTypeRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class CourseTypeServiceTest {

    @Mock
    private CourseTypeRepository courseTypeRepository;

    private CourseTypeService courseTypeService;

    private CourseType type;
    private CourseTypeRequest request;

    @BeforeEach
    void setUp() {
        type = CourseType.builder()
                .id(7)
                .name("Laboratory")
                .build();
        request = new CourseTypeRequest();
        request.setName("Lecture");
    }

    private void injectMocks() {
        courseTypeService = new CourseTypeService(courseTypeRepository);
    }

    @Test
    void shouldAddNewCourseType() {
        injectMocks();

        courseTypeService.addNewCourseType(request);
        ArgumentCaptor<CourseType> typeArgumentCaptor = ArgumentCaptor.forClass(CourseType.class);
        then(courseTypeRepository)
                .should(times(1))
                .save(typeArgumentCaptor.capture());
        CourseType type = typeArgumentCaptor.getValue();

        assertNotNull(type);
        assertEquals(request.getName(), type.getName());
        assertNull(type.getId());
    }

    @Test
    void shouldReturnCourseType() {
        given(courseTypeRepository.findById(7))
                .willReturn(Optional.of(type));
        injectMocks();

        CourseType result = courseTypeService.getCourseType(7);

        assertNotNull(result);
        assertThat(result, is(sameInstance(type)));
        assertEquals("Laboratory", result.getName());
        assertEquals(7, result.getId());
    }

    @Test
    void shouldThrowExceptionIfCourseTypeNotFound() {
        given(courseTypeRepository.findById(anyInt()))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> courseTypeService.getCourseType(7));
    }
}
