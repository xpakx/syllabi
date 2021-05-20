package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.repo.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(2)
                .username("smith123")
                .password("password")
                .roles(new HashSet<>())
                .build();
    }

    private void injectMocks() {
        userService = new UserService(userRepository);
    }

    @Test
    void shouldLoadUserById() {
        given(userRepository.findById(2))
                .willReturn(Optional.of(user));
        injectMocks();

        UserDetails result = userService.loadUserByUsername("2");

        assertEquals("2", result.getUsername());
        assertEquals("password", result.getPassword());
        assertThat(result.getAuthorities(), hasSize(0));
    }

    @Test
    void shouldThrowExceptionIfUserWithGivenIdNotFound() {
        given(userRepository.findById(anyInt()))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("2"));
    }

    @Test
    void shouldLoadUserByUsername() {
        given(userRepository.findByUsername("smith123"))
                .willReturn(Optional.of(user));
        injectMocks();

        UserDetails result = userService.loadUserToLogin("smith123");

        assertEquals("2", result.getUsername());
        assertEquals("password", result.getPassword());
        assertThat(result.getAuthorities(), hasSize(0));
    }

    @Test
    void shouldThrowExceptionIfUserWithGivenUsernameNotFound() {
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserToLogin("choamnomsky"));
    }
}