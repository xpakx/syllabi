package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.*;
import io.github.xpax.syllabi.entity.dto.SemesterRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.SemesterRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import io.github.xpax.syllabi.security.JwtTokenUtil;
import io.github.xpax.syllabi.service.UserService;
import io.restassured.http.ContentType;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SemesterControllerIntegrationTest {
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
    SemesterRepository semesterRepository;
    @Autowired
    CourseRepository courseRepository;

    private SemesterRequest semesterRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port+"").concat("/semesters");
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

        semesterRequest = new SemesterRequest();
        semesterRequest.setName("Edited Semester");
        semesterRequest.setNumber(1);
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
        semesterRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addSemester() {
        Semester semester = Semester.builder()
                .name("Semester")
                .number(1)
                .build();
        return semesterRepository.save(semester).getId();
    }

    private Integer addSemesterAndCourses() {
        List<Course> courses = new ArrayList<>();
        Semester semester = Semester.builder()
                .name("Semester")
                .number(1)
                .build();

        Course course1 = Course.builder()
                .name("Ethics")
                .semesters(Collections.singleton(semester))
                .build();
        courses.add(course1);
        Course course2 = Course.builder()
                .name("Epistemology")
                .semesters(Collections.singleton(semester))
                .build();
        courses.add(course2);
        Course course3 = Course.builder()
                .name("Metaphysics")
                .semesters(Collections.singleton(semester))
                .build();
        courses.add(course3);

        for(int i = 4; i<=25; i++) {
            Course dummyCourse = Course.builder()
                    .name("Dummy Course #"+i)
                    .semesters(Collections.singleton(semester))
                    .build();
            courses.add(dummyCourse);
        }

        semester.setCourses(new HashSet<>(courses));
        courseRepository.saveAll(courses);
        return semesterRepository.save(semester).getId();
    }

    @Test
    void shouldRespondWith401ToGetStudyGroupRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{semesterId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithGroup() {
        Integer id = addSemester();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{semesterId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Semester"));
    }

    @Test
    void shouldRespondWith404IfGroupNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{semesterId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToDeleteGroupRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/{semesterId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteGroupRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/{semesterId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteGroup() {
        Integer id = addSemester();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{semesterId}", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{semesterId}", id)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToUpdateSemesterRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .put(baseUrl + "/{semesterId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToUpdateSemesterRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(semesterRequest)
                .when()
                .put(baseUrl + "/{semesterId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithUpdatedSemester() {
        Integer id = addSemester();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(semesterRequest)
                .when()
                .put(baseUrl + "/{semesterId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Edited Semester"));

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{semesterId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Edited Semester"));
    }

    @Test
    void shouldRespondWith401ToGetSemesterCoursesRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{semesterId}/courses", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCoursesPageAndDefaultPaginationToGetSemesterCoursesRequest() {
        Integer id = addSemesterAndCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{semesterId}/courses", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("content[0].name", equalTo("Ethics"))
                .body("content[1].name", equalTo("Epistemology"))
                .body("content[2].name", equalTo("Metaphysics"))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithCoursesPageAndCustomPaginationToGetAllCoursesRequest() {
        Integer id = addSemesterAndCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl + "/{semesterId}/courses", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("content[0].name", equalTo("Dummy Course #16"))
                .body("numberOfElements", equalTo(5));
    }
}
