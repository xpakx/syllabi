package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.AuthenticationRequest;
import io.github.xpax.syllabi.entity.dto.ChangePasswordRequest;
import io.github.xpax.syllabi.entity.dto.RoleRequest;
import io.github.xpax.syllabi.repo.RoleRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import io.github.xpax.syllabi.security.JwtTokenUtil;
import io.github.xpax.syllabi.service.UserService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {
    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    UserService userService;


    private RoleRequest roleRequest;
    private ChangePasswordRequest passwordRequest;
    private ChangePasswordRequest passwordRequestDifferentPasswords;
    private ChangePasswordRequest passwordRequestBadOldPassword;
    private AuthenticationRequest authenticationRequestWithNewPassword;


    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port + "");
        User user = User.builder()
                .username("user1")
                .password("password")
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

        roleRequest = new RoleRequest();
        roleRequest.setRole("DUMMY_ROLE");

        passwordRequest = new ChangePasswordRequest();
        passwordRequest.setPasswordOld("password");
        passwordRequest.setPassword("newPassword");
        passwordRequest.setPasswordRe("newPassword");

        passwordRequestDifferentPasswords = new ChangePasswordRequest();
        passwordRequestDifferentPasswords.setPasswordOld("password");
        passwordRequestDifferentPasswords.setPassword("newPassword");
        passwordRequestDifferentPasswords.setPasswordRe("badPassword");

        passwordRequestBadOldPassword = new ChangePasswordRequest();
        passwordRequestBadOldPassword.setPasswordOld("badPassword");
        passwordRequestBadOldPassword.setPassword("newPassword");
        passwordRequestBadOldPassword.setPasswordRe("newPassword");

        authenticationRequestWithNewPassword = new AuthenticationRequest();
        authenticationRequestWithNewPassword.setUsername("user2");
        authenticationRequestWithNewPassword.setPassword("newPassword");
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
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

        Role dummy_role = new Role();
        dummy_role.setAuthority("DUMMY_ROLE");
        roleRepository.save(dummy_role);

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
    void shouldRespondWith401ToAddRoleForUserRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .post(baseUrl + "/users/{userId}/roles", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToAddRoleForUserRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(roleRequest)
                .when()
                .post(baseUrl + "/users/{userId}/roles", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldAddRoleForUser() {
        Integer id = addUsers();
        given()
                .contentType(ContentType.JSON)
                .body(roleRequest)
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .post(baseUrl + "/users/{userId}/roles", id)
                .then()
                .log()
                .everything()
                .statusCode(OK.value());

        Optional<User> user = userRepository.findById(id);
        assertTrue(user.isPresent());
        assertThat(user.get().getRoles(), contains(hasProperty("authority", equalTo("DUMMY_ROLE"))));
    }

    @Test
    void shouldRespondWith401ToGetAllUsersRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/users")
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToGetAllUsersRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users")
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithCoursesPageAndDefaultPaginationToGetAllCoursesRequest() {
        addUsers();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .get(baseUrl + "/users")
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("content[0].username", equalTo("user1"))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithCoursesPageAndCustomPaginationToGetAllCoursesRequest() {
        addUsers();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl + "/users")
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("content[0].username", equalTo("user15"))
                .body("numberOfElements", equalTo(5));
    }

    @Test
    void shouldRespondWith401ToDeleteUserRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/users/{userId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteUserRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/users/{userId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteUser() {
        Integer id = addUsers();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/users/{userId}", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .get(baseUrl + "/users/{userId}", id)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith404IfUserToDeleteNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/users/{userId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToGetUserRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/users/{userId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToGetUserRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithUser() {
        Integer id = addUsers();

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .get(baseUrl + "/users/{userId}", id)
                .then()
                .statusCode(OK.value())
                .body("username", equalTo("user2"));
    }

    @Test
    void shouldRespondWith404IfUserNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .get(baseUrl + "/users/{userId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToChangePasswordRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .put(baseUrl + "/users/{userId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToChangePasswordRequestIfWrongUser() {
        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(passwordRequest)
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .put(baseUrl + "/users/{userId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldChangePassword() {
        Integer id = addUsers();
        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(passwordRequest)
                .auth()
                .oauth2(tokenFor("user2"))
                .when()
                .put(baseUrl + "/users/{userId}", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(authenticationRequestWithNewPassword)
                .when()
                .post(baseUrl + "/authenticate")
                .then()
                .statusCode(OK.value())
                .body("$", hasKey("token"));
    }

    @Test
    void shouldNotChangePasswordIfPasswordsDoNotMatch() {
        Integer id = addUsers();
        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(passwordRequestDifferentPasswords)
                .auth()
                .oauth2(tokenFor("user2"))
                .when()
                .put(baseUrl + "/users/{userId}", id)
                .then()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void shouldNotChangePasswordIfWrongPassword() {
        Integer id = addUsers();
        given()
                .log()
                .uri()
                .contentType(ContentType.JSON)
                .body(passwordRequestBadOldPassword)
                .auth()
                .oauth2(tokenFor("user2"))
                .when()
                .put(baseUrl + "/users/{userId}", id)
                .then()
                .statusCode(BAD_REQUEST.value());
    }
}
