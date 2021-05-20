package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.AuthenticationRequest;
import io.github.xpax.syllabi.entity.dto.AuthenticationResponse;
import io.github.xpax.syllabi.error.JwtBadCredentialsException;
import io.github.xpax.syllabi.repo.UserRepository;
import io.github.xpax.syllabi.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private UserService userService;

    private AuthenticationService authenticationService;

    private AuthenticationRequest loginRequest;
    private UserDetails details;

    @BeforeEach
    void setUp() {
        loginRequest = new AuthenticationRequest();
        loginRequest.setPassword("password");
        loginRequest.setUsername("Wanda");

        details = new User("1", "password", new ArrayList<>());
    }

    private void injectMocks() {
        authenticationService =
                new AuthenticationService(authenticationManager, jwtTokenUtil, userService);
    }

    @Test
    void shouldThrowExceptionIfUsernameNotFound() {
        given(userService.loadUserToLogin(anyString()))
                .willThrow(new UsernameNotFoundException("User not found"));
        injectMocks();

        assertThrows(UsernameNotFoundException.class,
                () -> authenticationService.generateAuthenticationToken(loginRequest)
        );
    }

    @Test
    void shouldThrowExceptionIfBadCredentials() {
        given(userService.loadUserToLogin(anyString()))
                .willReturn(details);
        given(authenticationManager.authenticate(any(Authentication.class)))
                .willThrow(new BadCredentialsException(""));
        injectMocks();

        assertThrows(JwtBadCredentialsException.class,
                () -> authenticationService.generateAuthenticationToken(loginRequest)
        );
    }

    @Test
    void shouldReturnToken() {
        given(userService.loadUserToLogin(anyString()))
                .willReturn(details);
        given(jwtTokenUtil.generateToken(any(UserDetails.class)))
                .willReturn("token");
        injectMocks();

        AuthenticationResponse result = authenticationService.generateAuthenticationToken(loginRequest);

        assertEquals("token", result.getToken());
    }
}


