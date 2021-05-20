package io.github.xpax.syllabi.service;

import io.github.xpax.syllabi.entity.dto.AuthenticationRequest;
import io.github.xpax.syllabi.entity.dto.AuthenticationResponse;
import io.github.xpax.syllabi.entity.dto.RegistrationRequest;
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
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AuthenticationService authenticationService;

    private AuthenticationRequest loginRequest;
    private UserDetails details;
    private RegistrationRequest registrationRequest;
    private RegistrationRequest registrationRequestWithBadPassword;

    @BeforeEach
    void setUp() {
        loginRequest = new AuthenticationRequest();
        loginRequest.setPassword("password");
        loginRequest.setUsername("Wanda");

        details = new User("1", "password", new ArrayList<>());

        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("Wanda");
        registrationRequest.setPassword("password");
        registrationRequest.setPasswordRe("password");

        registrationRequestWithBadPassword = new RegistrationRequest();
        registrationRequestWithBadPassword.setUsername("Wanda");
        registrationRequestWithBadPassword.setPassword("password");
        registrationRequestWithBadPassword.setPasswordRe("pass");
    }

    private void injectMocks() {
        authenticationService = new AuthenticationService(authenticationManager, jwtTokenUtil, userService, userRepository, passwordEncoder);
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

    @Test
    void shouldThrowExceptionWhenRegisteringSecondUserWithSameUsername() {
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.of(new io.github.xpax.syllabi.entity.User()));
        injectMocks();

        assertThrows(ValidationException.class,
                () -> authenticationService.register(registrationRequest)
        );
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());
        injectMocks();

        assertThrows(ValidationException.class,
                () -> authenticationService.register(registrationRequestWithBadPassword)
        );
    }

    @Test
    void shouldSaveUser() {
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());
        given(userService.loadUserToLogin(anyString()))
                .willReturn(details);
        given(passwordEncoder.encode(anyString()))
                .willReturn("encoded password");
        injectMocks();

        authenticationService.register(registrationRequest);

        ArgumentCaptor<io.github.xpax.syllabi.entity.User> userCaptor = ArgumentCaptor.forClass(io.github.xpax.syllabi.entity.User.class);
        then(userRepository)
                .should(times(1))
                .save(userCaptor.capture());
        io.github.xpax.syllabi.entity.User user = userCaptor.getValue();

        assertNull(user.getId());
        assertEquals(registrationRequest.getUsername(), user.getUsername());
        assertEquals("encoded password", user.getPassword());
        assertThat(user.getRoles(), hasSize(0));
    }

    @Test
    void registrationShouldReturnToken() {
        given(userRepository.findByUsername(anyString()))
                .willReturn(Optional.empty());
        given(userService.loadUserToLogin(anyString()))
                .willReturn(details);
        given(jwtTokenUtil.generateToken(any(UserDetails.class)))
                .willReturn("token");
        injectMocks();

        AuthenticationResponse result = authenticationService.register(registrationRequest);

        assertEquals("token", result.getToken());
    }
}


