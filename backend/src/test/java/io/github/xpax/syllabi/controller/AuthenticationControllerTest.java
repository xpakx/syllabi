package io.github.xpax.syllabi.controller;


import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.AuthenticationRequest;
import io.github.xpax.syllabi.entity.dto.AuthenticationResponse;
import io.github.xpax.syllabi.entity.dto.RegistrationRequest;
import io.github.xpax.syllabi.error.JwtBadCredentialsException;
import io.github.xpax.syllabi.service.AuthenticationService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.MockMvcConfig;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;

    private AuthenticationRequest authenticationRequest;
    private AuthenticationResponse authenticationResponse;
    private RegistrationRequest registrationRequest;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("username");
        authenticationRequest.setPassword("password");

        authenticationResponse = new AuthenticationResponse("generatedToken", "3");

        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("username");
        registrationRequest.setPassword("password");
        registrationRequest.setPasswordRe("password");
    }

    private void injectMocks() {
        AuthenticationController userController =
                new AuthenticationController(authenticationService);
        RestAssuredMockMvc.standaloneSetup(userController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToAuthenticateUserRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(authenticationRequest)
                .when()
                .post("/authenticate")
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldAuthenticateUser() {
        BDDMockito.given(authenticationService.generateAuthenticationToken(any(AuthenticationRequest.class)))
                .willReturn(authenticationResponse);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(authenticationRequest)
                .when()
                .post("/authenticate")
                .then()
                .statusCode(OK.value())
                .body("token", equalTo("generatedToken"));
    }

    @Test
    void shouldNotAuthenticateIfBadCredentials() {
        BDDMockito.given(authenticationService.generateAuthenticationToken(any(AuthenticationRequest.class)))
                .willThrow(JwtBadCredentialsException.class);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(authenticationRequest)
                .when()
                .post("/authenticate")
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondToRegisterRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(registrationRequest)
                .when()
                .post("/register")
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldRegisterUser() {
        BDDMockito.given(authenticationService.register(any(RegistrationRequest.class)))
                .willReturn(authenticationResponse);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(registrationRequest)
                .when()
                .post("/register")
                .then()
                .statusCode(CREATED.value())
                .body("token", equalTo("generatedToken"));
    }
}
