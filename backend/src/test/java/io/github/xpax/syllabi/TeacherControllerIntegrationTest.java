package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.Job;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.UpdateJobRequest;
import io.github.xpax.syllabi.entity.dto.UserToTeacherRequest;
import io.github.xpax.syllabi.repo.JobRepository;
import io.github.xpax.syllabi.repo.TeacherRepository;
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

import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherControllerIntegrationTest {
    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    JobRepository jobRepository;

    private UserToTeacherRequest addTeacherRequest;
    private UpdateJobRequest jobRequest;

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


        addTeacherRequest = new UserToTeacherRequest();
        addTeacherRequest.setName("Adam");
        addTeacherRequest.setSurname("Smith");

        jobRequest = new UpdateJobRequest();
        jobRequest.setName("Researcher");
    }

    @AfterEach
    void cleanUp() {
        jobRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addUser() {
        User user = User.builder()
                .username("user2")
                .password("password")
                .roles(new HashSet<>())
                .build();
        return userRepository.save(user).getId();
    }

    private Integer addTeacher() {
        User user = User.builder()
                .username("user2")
                .password("password")
                .roles(new HashSet<>())
                .build();
        user = userRepository.save(user);

        Job job = Job.builder()
                .name("Lecturer")
                .build();
        Teacher teacher = Teacher.builder()
                .name("John")
                .surname("Agricola")
                .user(user)
                .job(job)
                .build();
        teacherRepository.save(teacher);

        return user.getId();
    }

    @Test
    void shouldRespondWith401ToAddTeacherRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .post(baseUrl + "/users/{userId}/teacher", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToAddTeacherRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(addTeacherRequest)
                .when()
                .post(baseUrl + "/users/{userId}/teacher", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithAddedTeacher() {
        Integer id = addUser();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(addTeacherRequest)
                .when()
                .post(baseUrl + "/users/{userId}/teacher", id)
                .then()
                .statusCode(CREATED.value())
                .body("name", equalTo("Adam"))
                .body("surname", equalTo("Smith"));

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/teacher", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Adam"))
                .body("surname", equalTo("Smith"));
    }

    @Test
    void shouldRespondWith401ToGetTeacherRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/users/{userId}/teacher", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithTeacher() {
        Integer id = addTeacher();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/teacher", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("John"))
                .body("surname", equalTo("Agricola"));
    }

    @Test
    void shouldRespondWith404IfTeacherNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/teacher", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToDeleteTeacherRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/users/{userId}/teacher", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteTeacherRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/users/{userId}/teacher", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteTeacher() {
        Integer id = addTeacher();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/users/{userId}/teacher", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/teacher", id)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToUpdateTeacherRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .put(baseUrl + "/users/{userId}/teacher", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToUpdateTeacherRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(addTeacherRequest)
                .when()
                .put(baseUrl + "/users/{userId}/teacher", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithUpdatedTeacher() {
        Integer id = addTeacher();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(addTeacherRequest)
                .when()
                .put(baseUrl + "/users/{userId}/teacher", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Adam"))
                .body("surname", equalTo("Smith"));

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/teacher", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Adam"))
                .body("surname", equalTo("Smith"));
    }

    @Test
    void shouldRespondWith401ToUpdateJobRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .put(baseUrl + "/users/{userId}/teacher/job", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToUpdateJobRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(jobRequest)
                .when()
                .put(baseUrl + "/users/{userId}/teacher/job", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithUpdatedJob() {
        Integer id = addTeacher();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(jobRequest)
                .when()
                .put(baseUrl + "/users/{userId}/teacher/job", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Researcher"));

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/teacher", id)
                .then()
                .statusCode(OK.value())
                .body("job.name", equalTo("Researcher"));
    }
}
