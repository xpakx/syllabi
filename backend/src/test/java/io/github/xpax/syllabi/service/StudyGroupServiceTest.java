package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.CourseType;
import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.dto.StudyGroupDetails;
import io.github.xpax.syllabi.entity.dto.StudyGroupForPage;
import io.github.xpax.syllabi.entity.dto.StudyGroupRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.CourseTypeRepository;
import io.github.xpax.syllabi.repo.CourseYearRepository;
import io.github.xpax.syllabi.repo.StudyGroupRepository;
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
import java.util.List;
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
class StudyGroupServiceTest {
    @Mock
    private CourseYearRepository courseYearRepository;
    @Mock
    private CourseTypeRepository courseTypeRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private StudyGroupRepository studyGroupRepository;

    private StudyGroupService studyGroupService;
    private StudyGroup group;
    private StudyGroupDetails groupDetails;
    private CourseYear courseYear;
    private CourseType courseType;
    private Teacher teacher0;
    private Teacher teacher1;
    private StudyGroupRequest request;
    private Page<StudyGroupForPage> page;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        group = StudyGroup.builder()
                .id(5)
                .build();
        this.groupDetails = factory.createProjection(StudyGroupDetails.class, group);

        courseYear = CourseYear.builder()
                .id(1)
                .build();
        courseType = CourseType.builder()
                .id(2)
                .name("Lecture")
                .build();
        teacher0 = Teacher.builder()
                .id(3)
                .name("Noam")
                .surname("Chomsky")
                .build();
        teacher1 = Teacher.builder()
                .id(9)
                .name("Nassim")
                .surname("Taleb")
                .build();
        request = new StudyGroupRequest();
        request.setCourseTypeId(2);
        request.setStudentLimit(20);
        List<Integer> teachers = new ArrayList<>();
        teachers.add(3);
        teachers.add(9);
        request.setTeachers(teachers);
        page = Page.empty();
    }

    private void injectMocks() {
        studyGroupService = new StudyGroupService(courseYearRepository, courseTypeRepository,
                teacherRepository, studyGroupRepository);
    }

    @Test
    void shouldAddStudyGroup() {
        given(courseYearRepository.getOne(1))
                .willReturn(courseYear);
        given(courseTypeRepository.getOne(2))
                .willReturn(courseType);
        given(teacherRepository.getOne(3))
                .willReturn(teacher0);
        given(teacherRepository.getOne(9))
                .willReturn(teacher1);
        injectMocks();

        studyGroupService.addNewStudyGroup(request, 1);

        ArgumentCaptor<StudyGroup> groupCaptor = ArgumentCaptor.forClass(StudyGroup.class);
        then(studyGroupRepository)
                .should(times(1))
                .save(groupCaptor.capture());
        StudyGroup addedGroup = groupCaptor.getValue();

        assertNotNull(addedGroup);
        assertNotNull(addedGroup.getYear());
        assertEquals(1, addedGroup.getYear().getId());

        assertNotNull(addedGroup.getType());
        assertEquals(2, addedGroup.getType().getId());
        assertEquals("Lecture", addedGroup.getType().getName());

        assertNotNull(addedGroup.getTeachers());
        assertThat(addedGroup.getTeachers(), hasSize(2));
        assertThat(addedGroup.getTeachers(), hasItem(hasProperty("id", equalTo(3))));
        assertThat(addedGroup.getTeachers(), hasItem(hasProperty("id", equalTo(9))));
        assertThat(addedGroup.getTeachers(), hasItem(hasProperty("surname", equalTo("Chomsky"))));
        assertThat(addedGroup.getTeachers(), hasItem(hasProperty("surname", equalTo("Taleb"))));

        assertEquals(20, addedGroup.getStudentLimit());

        assertNull(addedGroup.getId());
    }

    @Test
    void shouldAskRepositoryForGroups() {
        given(studyGroupRepository.findAllByYearId(anyInt(), any(PageRequest.class)))
                .willReturn(page);
        injectMocks();

        Page<StudyGroupForPage> result = studyGroupService.getAllGroupsByCourseYear(5, 0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);

        then(studyGroupRepository)
                .should(times(1))
                .findAllByYearId(integerCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer yearId = integerCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());

        assertThat(result, is(sameInstance(page)));

        assertEquals(5, yearId);
    }

    @Test
    void shouldReturnStudyGroup() {
        given(studyGroupRepository.findProjectedById(5))
                .willReturn(Optional.of(groupDetails));
        injectMocks();

        StudyGroupDetails result = studyGroupService.getStudyGroup(5);

        assertNotNull(result);
        assertEquals(5, result.getId());
    }

    @Test
    void shouldThrowExceptionIfStudyGroupNotFound() {
        given(studyGroupRepository.findProjectedById(anyInt()))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> studyGroupService.getStudyGroup(5));
    }
}
