package io.github.xpax.syllabi.security;

import io.github.xpax.syllabi.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class JwtRequestFilterTest {
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;

    private JwtRequestFilter jwtRequestFilter;

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    private UserDetails user;

    @Mock
    SecurityContext securityContext;
    @Mock
    Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);

        user = new User("5", "password", new ArrayList<>());
    }

    private void injectMocks() {
        jwtRequestFilter = new JwtRequestFilter(userService, jwtTokenUtil);
    }

    @Test
    void shouldNotValidateIfNoAuthorizationHeader() throws Exception {
        given(request.getHeader("Authorization"))
                .willReturn(null);
        injectMocks();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        then(jwtTokenUtil)
                .shouldHaveNoInteractions();
        then(securityContext)
                .shouldHaveNoInteractions();

    }

    @Test
    void shouldNotValidateIfAuthorizationHeaderDoesNotStartWithBearer() throws Exception {
        given(request.getHeader("Authorization"))
                .willReturn("token");
        injectMocks();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        then(jwtTokenUtil)
                .shouldHaveNoInteractions();
        then(securityContext)
                .shouldHaveNoInteractions();
    }

    @Test
    void shouldNotValidateIfUnableToGetJwtToken() throws Exception {
        given(request.getHeader("Authorization"))
                .willReturn("Bearer token");
        given(jwtTokenUtil.getUsernameFromToken("token"))
                .willThrow(IllegalArgumentException.class);
        injectMocks();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        then(jwtTokenUtil)
                .should(times(0))
                .validateToken(anyString(), any(UserDetails.class));
        then(securityContext)
                .shouldHaveNoInteractions();
    }

    @Test
    void shouldNotValidateIfJwtTokenExpired() throws Exception {
        given(request.getHeader("Authorization"))
                .willReturn("Bearer token");
        given(jwtTokenUtil.getUsernameFromToken("token"))
                .willThrow(ExpiredJwtException.class);
        injectMocks();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        then(jwtTokenUtil)
                .should(times(0))
                .validateToken(anyString(), any(UserDetails.class));
        then(securityContext)
                .shouldHaveNoInteractions();
    }

    @Test
    void shouldNotValidateTokenIfAuthenticationExistsInSecurityContext() throws Exception {
        given(request.getHeader("Authorization"))
                .willReturn("Bearer token");
        given(jwtTokenUtil.getUsernameFromToken("token"))
                .willReturn("5");
        given(securityContext.getAuthentication())
                .willReturn(authentication);

        injectMocks();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        then(jwtTokenUtil)
                .should(times(0))
                .validateToken(anyString(), any(UserDetails.class));
        then(securityContext)
                .should(times(0))
                .setAuthentication(any(Authentication.class));

    }

    @Test
    void shouldValidateToken() throws Exception {
        given(request.getHeader("Authorization"))
                .willReturn("Bearer token");
        given(jwtTokenUtil.getUsernameFromToken("token"))
                .willReturn("5");
        given(securityContext.getAuthentication())
                .willReturn(null);
        given(userService.loadUserByUsername("5"))
                .willReturn(user);

        injectMocks();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        then(userService)
                .should(times(1))
                .loadUserByUsername("5");

        ArgumentCaptor<UserDetails> detailsCaptor = ArgumentCaptor.forClass(UserDetails.class);
        then(jwtTokenUtil)
                .should(times(1))
                .validateToken(anyString(), detailsCaptor.capture());

        UserDetails details = detailsCaptor.getValue();
        assertNotNull(details);
        assertEquals("5", details.getUsername());
        assertEquals("password", details.getPassword());
        assertThat(details.getAuthorities(), hasSize(0));

    }

    @Test
    void shouldNotAuthenticateIfTokenValidationFailed() throws Exception {
        given(request.getHeader("Authorization"))
                .willReturn("Bearer token");
        given(jwtTokenUtil.getUsernameFromToken("token"))
                .willReturn("5");
        given(securityContext.getAuthentication())
                .willReturn(null);
        given(userService.loadUserByUsername("5"))
                .willReturn(user);
        given(jwtTokenUtil.validateToken(anyString(), any(UserDetails.class)))
                .willReturn(false);

        injectMocks();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        then(securityContext)
                .should(times(0))
                .setAuthentication(any(Authentication.class));

    }

    @Test
    void shouldAuthenticateIfTokenValidationPassed() throws Exception {
        given(request.getHeader("Authorization"))
                .willReturn("Bearer token");
        given(jwtTokenUtil.getUsernameFromToken("token"))
                .willReturn("5");
        given(securityContext.getAuthentication())
                .willReturn(null);
        given(userService.loadUserByUsername("5"))
                .willReturn(user);
        given(jwtTokenUtil.validateToken(anyString(), any(UserDetails.class)))
                .willReturn(true);

        injectMocks();

        jwtRequestFilter.doFilterInternal(request, response, filterChain);

        ArgumentCaptor<Authentication> authCaptor = ArgumentCaptor.forClass(Authentication.class);
        then(securityContext)
                .should(times(1))
                .setAuthentication(authCaptor.capture());

        Authentication auth = authCaptor.getValue();

        assertNotNull(auth);
        assertEquals("5", auth.getName());

    }
}