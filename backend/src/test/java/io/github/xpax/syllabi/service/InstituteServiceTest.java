package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.dto.*;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

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
class InstituteServiceTest {
    @Mock
    private InstituteRepository instituteRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private ProgramRepository programRepository;

    private InstituteService instituteService;
    private Page<InstituteForPage> page;
    private Institute institute;
    private InstituteDetails instituteDet;
    private InstituteRequest request;
    private Page<CourseForPage> coursePage;
    private Page<ProgramForPage> programPage;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        page = Page.empty();
        institute = Institute.builder()
                .id(3)
                .name("Institute of Computer Science")
                .build();
        instituteDet = factory.createProjection(InstituteDetails.class, institute);
        request = new InstituteRequest();
        request.setName("Institute of Artificial Intelligence");
        request.setParentId(3);
        coursePage = Page.empty();
        programPage = Page.empty();
    }

    private void injectMocks() {
        instituteService = new InstituteService(instituteRepository, courseRepository, programRepository);
    }

    @Test
    void shouldAskRepositoryForPageOfInstitutes() {
        given(instituteRepository.findProjectedBy(any(PageRequest.class)))
                .willReturn(page);
        injectMocks();

        Page<InstituteForPage> result = instituteService.getAllInstitutes(0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);

        then(instituteRepository)
                .should(times(1))
                .findProjectedBy(pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());

        assertThat(result, is(sameInstance(page)));
    }

    @Test
    void shouldDeleteInstitute() {
        injectMocks();

        instituteService.deleteInstitute(5);

        then(instituteRepository)
                .should(times(1))
                .deleteById(5);
    }

    @Test
    void shouldReturnInstitute() {
        given(instituteRepository.findProjectedById(3))
                .willReturn(Optional.of(instituteDet));
        injectMocks();

        InstituteDetails result = instituteService.getInstitute(3);

        assertNotNull(result);
        assertEquals("Institute of Computer Science", result.getName());
        assertEquals(3, result.getId());
    }

    @Test
    void shouldThrowExceptionIfInstituteNotFound() {
        given(instituteRepository.findProjectedById(anyInt()))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> instituteService.getInstitute(3));
    }

    @Test
    void shouldAddNewInstitute() {
        given(instituteRepository.getOne(3))
                .willReturn(institute);
        injectMocks();

        instituteService.addNewInstitute(request);

        ArgumentCaptor<Institute> instituteCaptor = ArgumentCaptor.forClass(Institute.class);
        then(instituteRepository)
                .should(times(1))
                .save(instituteCaptor.capture());
        Institute addedInstitute = instituteCaptor.getValue();

        assertNotNull(addedInstitute);
        assertNotNull(addedInstitute.getParent());
        assertEquals(3, addedInstitute.getParent().getId());
        assertEquals("Institute of Computer Science", addedInstitute.getParent().getName());
        assertEquals("Institute of Artificial Intelligence", addedInstitute.getName());
        assertNull(addedInstitute.getId());
    }

    @Test
    void shouldUpdateInstitute() {
        given(instituteRepository.getOne(3))
                .willReturn(institute);
        injectMocks();

        instituteService.updateInstitute(request, 4);

        ArgumentCaptor<Institute> instituteCaptor = ArgumentCaptor.forClass(Institute.class);
        then(instituteRepository)
                .should(times(1))
                .save(instituteCaptor.capture());
        Institute addedInstitute = instituteCaptor.getValue();

        assertNotNull(addedInstitute);
        assertNotNull(addedInstitute.getParent());
        assertEquals(3, addedInstitute.getParent().getId());
        assertEquals("Institute of Computer Science", addedInstitute.getParent().getName());
        assertEquals("Institute of Artificial Intelligence", addedInstitute.getName());
        assertEquals(4, addedInstitute.getId());
    }

    @Test
    void shouldAskRepositoryForOrganizedCourses() {
        given(courseRepository.findByOrganizerId(anyInt(), any(PageRequest.class)))
                .willReturn(coursePage);
        injectMocks();

        instituteService.getAllCoursesByOrganizerId(0, 20, 5);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> instituteIdCaptor = ArgumentCaptor.forClass(Integer.class);

        then(courseRepository)
                .should(times(1))
                .findByOrganizerId(instituteIdCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer organizerId = instituteIdCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(5, organizerId);
    }

    @Test
    void shouldAskRepositoryForOrganizedPrograms() {
        given(programRepository.findByOrganizerId(anyInt(), any(PageRequest.class)))
                .willReturn(programPage);
        injectMocks();

        instituteService.getAllProgramsByInstitute(0, 20, 5);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);
        ArgumentCaptor<Integer> instituteIdCaptor = ArgumentCaptor.forClass(Integer.class);

        then(programRepository)
                .should(times(1))
                .findByOrganizerId(instituteIdCaptor.capture(), pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();
        Integer organizerId = instituteIdCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());
        assertEquals(5, organizerId);
    }

    @Test
    void shouldAskRepositoryForPageOfChildren() {
        given(instituteRepository.findByParentId(anyInt(), any(PageRequest.class)))
                .willReturn(page);
        injectMocks();

        Page<InstituteForPage> result = instituteService.getAllChildrenByOrganizerId(0, 20, 5);

        ArgumentCaptor<Pageable> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);

        then(instituteRepository)
                .should(times(1))
                .findByParentId(anyInt(), pageRequestCaptor.capture());
        Pageable pageRequest = pageRequestCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());

        assertThat(result, is(sameInstance(page)));
    }
}
