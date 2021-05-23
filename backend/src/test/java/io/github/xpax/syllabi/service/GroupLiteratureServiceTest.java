package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.GroupLiterature;
import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.entity.dto.LiteratureRequest;
import io.github.xpax.syllabi.error.NotFoundException;
import io.github.xpax.syllabi.repo.GroupLiteratureRepository;
import io.github.xpax.syllabi.repo.StudyGroupRepository;
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
class GroupLiteratureServiceTest {
    @Mock
    private GroupLiteratureRepository groupLiteratureRepository;
    @Mock
    private StudyGroupRepository studyGroupRepository;

    private GroupLiteratureService groupLiteratureService;
    private Page<LiteratureForPage> groupLiteraturePage;
    private GroupLiterature literature;
    private StudyGroup group;
    private LiteratureRequest request;

    @BeforeEach
    void setUp() {
        groupLiteraturePage = Page.empty();
        literature = GroupLiterature.builder()
                .id(0)
                .author("Dante Alighieri")
                .title("Divine Comedy")
                .build();
        group = StudyGroup.builder()
                .id(1)
                .build();
        request = new LiteratureRequest();
        request.setAuthor("Stephen Toulmin");
        request.setTitle("The Uses of Argument");
    }

    private void injectMocks() {
        groupLiteratureService = new GroupLiteratureService(groupLiteratureRepository, studyGroupRepository);
    }

    @Test
    void shouldAskRepositoryForPageOfLiterature() {
        given(groupLiteratureRepository.findAllByStudyGroupId(anyInt(), any(PageRequest.class)))
                .willReturn(groupLiteraturePage);
        injectMocks();

        Page<LiteratureForPage> result = groupLiteratureService.getAllLiterature(0, 20, 0);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> courseIdCaptor = ArgumentCaptor.forClass(Integer.class);

        then(groupLiteratureRepository)
                .should(times(1))
                .findAllByStudyGroupId(courseIdCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer courseId = courseIdCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(0, courseId);

        assertThat(result, is(sameInstance(groupLiteraturePage)));
    }

    @Test
    void shouldDeleteLiterature() {
        injectMocks();
        groupLiteratureService.deleteLiterature(0);

        then(groupLiteratureRepository)
                .should(times(1))
                .deleteById(0);
    }

    @Test
    void shouldReturnLiteratureIfFound() {
        given(groupLiteratureRepository.findById(0))
                .willReturn(Optional.of(literature));
        injectMocks();

        GroupLiterature result = groupLiteratureService.getLiterature(0);

        assertNotNull(result);
        assertEquals("Dante Alighieri", result.getAuthor());
        assertEquals("Divine Comedy", result.getTitle());
        assertEquals(0, result.getId());
    }

    @Test
    void shouldThrowExceptionIfLiteratureNotFound() {
        given(groupLiteratureRepository.findById(0))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> groupLiteratureService.getLiterature(0));
    }

    @Test
    void shouldAddNewLiteratureToStudyGroup() {
        given(studyGroupRepository.getOne(1))
                .willReturn(group);
        injectMocks();

        groupLiteratureService.addNewLiterature(request, 1);

        ArgumentCaptor<GroupLiterature> literatureCaptor = ArgumentCaptor.forClass(GroupLiterature.class);
        then(groupLiteratureRepository)
                .should(times(1))
                .save(literatureCaptor.capture());
        GroupLiterature groupLiterature = literatureCaptor.getValue();

        assertNotNull(groupLiterature);
        assertEquals("Stephen Toulmin", groupLiterature.getAuthor());
        assertEquals("The Uses of Argument", groupLiterature.getTitle());
        assertNotNull(groupLiterature.getStudyGroup());
        assertEquals(1, groupLiterature.getStudyGroup().getId());
    }
}
