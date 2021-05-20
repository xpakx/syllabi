package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.*;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.repo.*;
import io.github.xpax.syllabi.security.JwtTokenUtil;
import io.github.xpax.syllabi.service.UserService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerIntegrationTest {
    private final String baseUrl;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationRequest authenticationRequest;
    private final AuthenticationRequest badAuthenticationRequest;
    private final AuthenticationRequest authenticationRequestNonexistentUser;
    private final RegistrationRequest registrationRequest;
    private final RegistrationRequest registrationRequestDifferentPasswords;
    private final RegistrationRequest registrationRequestUserExists;

    @Autowired
    public AuthenticationControllerIntegrationTest(JwtTokenUtil jwtTokenUtil, UserRepository userRepository,
                                         UserService userService,
                                         PasswordEncoder passwordEncoder, Environment env) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername("user1");
        authenticationRequest.setPassword("password");

        badAuthenticationRequest = new AuthenticationRequest();
        badAuthenticationRequest.setUsername("user1");
        badAuthenticationRequest.setPassword("badPassword");

        authenticationRequestNonexistentUser = new AuthenticationRequest();
        authenticationRequestNonexistentUser.setUsername("user2");
        authenticationRequestNonexistentUser.setPassword("password");

        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("user2");
        registrationRequest.setPassword("password");
        registrationRequest.setPasswordRe("password");
        registrationRequestDifferentPasswords = new RegistrationRequest();
        registrationRequestDifferentPasswords.setUsername("user2");
        registrationRequestDifferentPasswords.setPassword("password");
        registrationRequestDifferentPasswords.setPasswordRe("badPassword");
        registrationRequestUserExists = new RegistrationRequest();
        registrationRequestUserExists.setUsername("user1");
        registrationRequestUserExists.setPassword("password");
        registrationRequestUserExists.setPasswordRe("password");

        String port = env.getProperty("local.server.port");
        port = port == null ? "" : port;
        baseUrl = "http://localhost".concat(":").concat(port);
    }

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username("user1")
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<>())
                .build();
        userRepository.save(user);

        Role adminRole = new Role();
        adminRole.setAuthority("ROLE_USER_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        User admin = User.builder()
                .username("admin1")
                .password("password")
                .roles(roles)
                .build();
        userRepository.save(admin);

        Role adminRole2 = new Role();
        adminRole2.setAuthority("ROLE_COURSE_ADMIN");
        Set<Role> roles2 = new HashSet<>();
        roles2.add(adminRole2);
        User admin2 = User.builder()
                .username("admin2")
                .password("password")
                .roles(roles2)
                .build();
        userRepository.save(admin2);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addUsers() {
        User user = User.builder()
                .username("user2")
                .password(passwordEncoder.encode("password"))
                .roles(new HashSet<>())
                .build();
        Integer userId = userRepository.save(user).getId();


        for(int i = 3; i<=25; i++) {
            User dummyUser = User.builder()
                    .username("user"+i)
                    .password("password")
                    .build();
            userRepository.save(dummyUser);
        }
        return userId;
    }

    @Test
    void shouldAuthenticate() {
        String token = given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(authenticationRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .statusCode(OK.value())
                .body("$", hasKey("token"))
                .extract()
                .jsonPath()
                .getString("token");
    }

    @Test
    void shouldNotAuthenticateIfBadPassword() {
        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(badAuthenticationRequest)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .statusCode(UNAUTHORIZED.value())
                .body("$", not(hasKey("token")));
    }

    @Test
    void shouldNotAuthenticateIfUserNotFound() {
        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(authenticationRequestNonexistentUser)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .statusCode(UNAUTHORIZED.value())
                .body("$", not(hasKey("token")));
    }

    @Test
    void shouldRegisterUser() {
        String token = given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(registrationRequest)
                .when()
                .post(baseUrl + "/register")
                .then()
                .statusCode(CREATED.value())
                .body("$", hasKey("token"))
                .extract()
                .jsonPath()
                .getString("token");
    }

    @Test
    void shouldNotRegisterIfPasswordsDoNotMatch() {
        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(registrationRequestDifferentPasswords)
                .when()
                .post(baseUrl + "/register")
                .then()
                .log()
                .body()
                .statusCode(BAD_REQUEST.value())
                .body("$", not(hasKey("token")))
                .body("message", containsString("don't match"));
    }

    @Test
    void shouldNotRegisterIfUsernameAlreadyUsed() {
        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(registrationRequestUserExists)
                .when()
                .post(baseUrl + "/register")
                .then()
                .log()
                .body()
                .statusCode(BAD_REQUEST.value())
                .body("$", not(hasKey("token")))
                .body("message", containsString("exists"));
    }
}