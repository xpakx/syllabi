package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.CourseType;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.CourseTypeRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.CourseTypeRepository;
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
class CourseTypeControllerIntegrationTest {

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
    CourseRepository courseRepository;
    @Autowired
    CourseTypeRepository courseTypeRepository;
    private CourseTypeRequest courseTypeRequest;
    private CourseTypeRequest updateCourseTypeRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port+"").concat("/types");
        User user = User.builder()
                .username("user1")
                .password("password")
                .roles(new HashSet<>())
                .build();
        userRepository.save(user);

        Role adminRole = new Role();
        adminRole.setAuthority("ROLE_COURSE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        User admin = User.builder()
                .username("admin1")
                .password("password")
                .roles(roles)
                .build();
        userRepository.save(admin);

        courseTypeRequest = new CourseTypeRequest();
        courseTypeRequest.setName("Lecture");

        updateCourseTypeRequest = new CourseTypeRequest();
        updateCourseTypeRequest.setName("Edited lecture");
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        courseTypeRepository.deleteAll();
        courseRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addCourseTypes() {
        CourseType lecture = CourseType.builder()
                .name("Lecture")
                .build();
        Integer lectureId = courseTypeRepository.save(lecture).getId();

        for(int i = 2; i<=25; i++) {
            CourseType dummyCourseType = CourseType.builder()
                    .name("Dummy Course Type #"+i)
                    .build();
            courseTypeRepository.save(dummyCourseType);
        }

        return lectureId;
    }

    @Test
    void shouldRespondWith401ToAddCourseTypeRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .post(baseUrl)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToAddCourseTypeRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(courseTypeRequest)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithAddedCourseType() {
        Integer addedCourseTypeId = given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(courseTypeRequest)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(CREATED.value())
                .body("name", equalTo("Lecture"))
                .extract()
                .jsonPath()
                .getInt("id");

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}", addedCourseTypeId)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Lecture"));
    }

    @Test
    void shouldRespondWith401ToGetCourseTypeRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{courseTypeId}", 1)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCourseType() {
        Integer id = addCourseTypes();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseTypeId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Lecture"));
    }

    @Test
    void shouldRespondWith404IfCourseTypeNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseTypeId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToDeleteCourseTypeRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/{courseTypeId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteCourseTypeRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/{courseTypeId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteCourseType() {
        Integer id = addCourseTypes();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{courseTypeId}", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseTypeId}", id)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith404IfCourseTypeToDeleteNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{courseTypeId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToUpdateCourseTypeRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .put(baseUrl + "/{courseTypeId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToUpdateCourseTypeRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(updateCourseTypeRequest)
                .when()
                .put(baseUrl + "/{courseTypeId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithUpdatedCourseType() {
        Integer id = addCourseTypes();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(updateCourseTypeRequest)
                .when()
                .put(baseUrl + "/{courseTypeId}", id)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(id))
                .body("name", equalTo("Edited lecture"));

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Edited lecture"));
    }

}
