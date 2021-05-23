package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.InstituteForPage;
import io.github.xpax.syllabi.repo.InstituteRepository;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class InstituteServiceTest {
    @Mock
    private InstituteRepository instituteRepository;

    private InstituteService instituteService;
    private Page<InstituteForPage> page;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        page = Page.empty();
    }

    private void injectMocks() {
        instituteService = new InstituteService(instituteRepository);
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
}
