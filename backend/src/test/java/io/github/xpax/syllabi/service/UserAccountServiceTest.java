package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.ChangePasswordRequest;
import io.github.xpax.syllabi.entity.dto.UserWithoutPassword;
import io.github.xpax.syllabi.error.NotFoundException;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ValidationException;
import java.util.HashSet;
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
public class UserAccountServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private UserAccountService userAccountService;

    private Page<UserWithoutPassword> userPage;;
    private User userWithId0;
    private UserWithoutPassword userWithId0WithoutPassword;
    private ChangePasswordRequest okPasswordRequest;
    private ChangePasswordRequest badPasswordRequest;
    private ChangePasswordRequest passwordRequestWithBadOldPassword;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        userPage = Page.empty();
        userWithId0 = User.builder()
                .id(0)
                .username("marvinminsky")
                .password("oldEncoded")
                .roles(new HashSet<>())
                .build();

        this.userWithId0WithoutPassword = factory.createProjection(UserWithoutPassword.class, userWithId0);

        okPasswordRequest = new ChangePasswordRequest();
        okPasswordRequest.setPasswordOld("old");
        okPasswordRequest.setPassword("new");
        okPasswordRequest.setPasswordRe("new");

        badPasswordRequest = new ChangePasswordRequest();
        badPasswordRequest.setPasswordOld("old");
        badPasswordRequest.setPassword("new");
        badPasswordRequest.setPasswordRe("nwe");

        passwordRequestWithBadOldPassword = new ChangePasswordRequest();
        passwordRequestWithBadOldPassword.setPasswordOld("odl");
        passwordRequestWithBadOldPassword.setPassword("new");
        passwordRequestWithBadOldPassword.setPassword("new");
    }

    private void injectMocks() {
        userAccountService = new UserAccountService(userRepository, passwordEncoder);
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

    @Test
    void shouldDeleteUser() {
        injectMocks();
        userAccountService.deleteUser(5);
        then(userRepository)
                .should(times(1))
                .deleteById(5);
    }

    @Test
    void shouldReturnUser() {
        given(userRepository.findAllProjectedById(0))
                .willReturn(Optional.of(userWithId0WithoutPassword));
        injectMocks();

        UserWithoutPassword result = userAccountService.getUser(0);

        assertEquals(0, result.getId());
        assertEquals("marvinminsky", result.getUsername());
        assertThat(result.getRoles(), hasSize(0));
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {
        given(userRepository.findAllProjectedById(anyInt()))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> userAccountService.getUser(0));
    }

    @Test
    void shouldChangePassword() {
        given(passwordEncoder.encode("new"))
                .willReturn("newEncoded");
        given(passwordEncoder.matches("old", "oldEncoded"))
                .willReturn(true);
        given(userRepository.findById(0))
                .willReturn(Optional.of(userWithId0));
        injectMocks();

        userAccountService.changePassword(okPasswordRequest, 0);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        then(userRepository)
                .should(times(1))
                .save(userCaptor.capture());
        User updatedUser = userCaptor.getValue();

        assertNotNull(updatedUser);
        assertEquals("marvinminsky", updatedUser.getUsername());
        assertThat(updatedUser.getRoles(), hasSize(0));
        assertEquals("newEncoded", updatedUser.getPassword());
        assertEquals(0, updatedUser.getId());
    }

    @Test
    void shouldThrowExceptionIfPasswordsDoNotMatch() {
        injectMocks();

        assertThrows(ValidationException.class, () -> userAccountService.changePassword(badPasswordRequest, 0));
    }

    @Test
    void shouldThrowExceptionIfOldPasswordDoNotMatch() {
        injectMocks();

        assertThrows(ValidationException.class, () -> userAccountService.changePassword(passwordRequestWithBadOldPassword, 0));
    }

    @Test
    void shouldThrowExceptionIfTryToChangeNonexistentUserPassword() {
        given(userRepository.findById(0))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(NotFoundException.class, () -> userAccountService.changePassword(okPasswordRequest, 0));
    }
}
