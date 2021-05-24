package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.UserWithoutPassword;
import io.github.xpax.syllabi.repo.UserRepository;
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
public class UserAccountServiceTest {
    @Mock
    private UserRepository userRepository;

    private UserAccountService userAccountService;


    private Page<UserWithoutPassword> userPage;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        userPage = Page.empty();
    }

    private void injectMocks() {
        userAccountService = new UserAccountService(userRepository);
    }

    @Test
    void shouldAskRepositoryForUsers() {
        given(userRepository.findAllProjectedBy(any(PageRequest.class)))
                .willReturn(userPage);
        injectMocks();

        Page<UserWithoutPassword> result = userAccountService.getAllUsers(0, 20);

        ArgumentCaptor<PageRequest> pageRequestCaptor = ArgumentCaptor.forClass(PageRequest.class);

        then(userRepository)
                .should(times(1))
                .findAllProjectedBy(pageRequestCaptor.capture());
        PageRequest pageRequest = pageRequestCaptor.getValue();

        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(20, pageRequest.getPageSize());

        assertThat(result, is(sameInstance(userPage)));
    }
}
