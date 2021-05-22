package io.github.xpax.syllabi.security;

import io.github.xpax.syllabi.repo.CourseYearRepository;
import io.github.xpax.syllabi.repo.StudyGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PermissionEvaluatorTest {
    @Mock
    private StudyGroupRepository studyGroupRepository;
    @Mock
    private CourseYearRepository courseYearRepository;

    private PermissionEvaluator permissionEvaluator;

    @BeforeEach
    void setUp() {
    }

    private void injectMocks() {
        permissionEvaluator = new PermissionEvaluator(studyGroupRepository, courseYearRepository);
    }

    @Test
    void shouldAllowEditingStudyGroup() {
        given(studyGroupRepository.existsTeacherByStudyGroupWithUserId(anyInt(), anyInt()))
                .willReturn(true);
        injectMocks();

        boolean result = permissionEvaluator.canEditStudyGroup(5, "3");

        then(studyGroupRepository)
                .should(times(1))
                .existsTeacherByStudyGroupWithUserId(5,3);
        assertTrue(result);
    }

    @Test
    void shouldNotAllowEditingStudyGroup() {
        given(studyGroupRepository.existsTeacherByStudyGroupWithUserId(anyInt(), anyInt()))
                .willReturn(false);
        injectMocks();

        boolean result = permissionEvaluator.canEditStudyGroup(5, "3");

        assertFalse(result);
    }

    @Test
    void shouldAllowViewingStudyGroup() {
        given(studyGroupRepository.existsTeacherByStudyGroupWithUserId(anyInt(), anyInt()))
                .willReturn(true);
        injectMocks();

        boolean result = permissionEvaluator.canViewStudyGroup(5, "3");

        then(studyGroupRepository)
                .should(times(1))
                .existsTeacherByStudyGroupWithUserId(5,3);
        assertTrue(result);
    }

    @Test
    void shouldNotAllowViewingStudyGroup() {
        given(studyGroupRepository.existsTeacherByStudyGroupWithUserId(anyInt(), anyInt()))
                .willReturn(false);
        injectMocks();

        boolean result = permissionEvaluator.canViewStudyGroup(5, "3");

        assertFalse(result);
    }

    @Test
    void shouldAllowEditingCourseYear() {
        given(courseYearRepository.existsTeacherByCourseYearIdAndUserId(anyInt(), anyInt()))
                .willReturn(true);
        injectMocks();

        boolean result = permissionEvaluator.canEditCourseYear(5, "3");

        then(courseYearRepository)
                .should(times(1))
                .existsTeacherByCourseYearIdAndUserId(5,3);
        assertTrue(result);
    }

    @Test
    void shouldNotAllowEditingCourseYear() {
        given(courseYearRepository.existsTeacherByCourseYearIdAndUserId(anyInt(), anyInt()))
                .willReturn(false);
        injectMocks();

        boolean result = permissionEvaluator.canEditCourseYear(5, "3");

        assertFalse(result);
    }

    @Test
    void shouldAllowViewingCourseYear() {
        given(courseYearRepository.existsTeacherByCourseYearIdAndUserId(anyInt(), anyInt()))
                .willReturn(true);
        injectMocks();

        boolean result = permissionEvaluator.canViewCourseYear(5, "3");

        then(courseYearRepository)
                .should(times(1))
                .existsTeacherByCourseYearIdAndUserId(5,3);
        assertTrue(result);
    }

    @Test
    void shouldNotAllowViewingCourseYear() {
        given(courseYearRepository.existsTeacherByCourseYearIdAndUserId(anyInt(), anyInt()))
                .willReturn(false);
        injectMocks();

        boolean result = permissionEvaluator.canViewCourseYear(5, "3");

        assertFalse(result);
    }
}
