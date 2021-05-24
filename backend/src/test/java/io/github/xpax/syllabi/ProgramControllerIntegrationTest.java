package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.ProgramRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.ProgramRepository;
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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProgramControllerIntegrationTest {
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
    ProgramRepository programRepository;

    private ProgramRequest programRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port+"").concat("/programs");
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

        programRequest = new ProgramRequest();
        programRequest.setName("Cognitive Science");
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
        programRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addCourses() {
        Set<Course> courses = new HashSet<>();
        Course course1 = Course.builder()
                .name("Introduction to Cognitive Science")
                .programs(new HashSet<>())
                .build();
        courses.add(course1);
        Course course2 = Course.builder()
                .name("Pragmatics")
                .programs(new HashSet<>())
                .build();
        courses.add(course2);
        Course course3 = Course.builder()
                .name("Logic I")
                .programs(new HashSet<>())
                .build();
        courses.add(course3);

        for(int i = 4; i<=25; i++) {
            Course dummyCourse = Course.builder()
                    .name("Dummy Course #"+i)
                    .programs(new HashSet<>())
                    .build();
            courses.add(dummyCourse);
        }

        Program program = Program.builder()
                .name("Cognitive Science")
                .courses(courses)
                .build();
        for(Course c : courses) {
            c.setPrograms(Collections.singleton(program));
        }

        courseRepository.saveAll(courses);
        return programRepository.save(program).getId();
    }

    private Integer addPrograms() {
        Program program = Program.builder()
                .name("Cognitive Science")
                .build();

        for(int i = 1; i<=25; i++) {
            Program dummyProgram = Program.builder()
                    .name("Dummy Course #"+i)
                    .build();
            programRepository.save(dummyProgram);
        }

        return programRepository.save(program).getId();
    }

    @Test
    void shouldRespondWith401ToAddProgramRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .post(baseUrl)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToAddProgramRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithAddedProgram() {
        Integer addedCourseId = given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(CREATED.value())
                .body("name", equalTo("Cognitive Science"))
                .extract()
                .jsonPath()
                .getInt("id");

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{programId}", addedCourseId)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Cognitive Science"));
    }

    @Test
    void shouldRespondWith401ToGetAllCoursesForProgramRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{programId}/courses", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCoursesPageAndDefaultPaginationToGetAllCoursesForProgramRequest() {
        Integer id = addCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{programId}/courses", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithCoursesPageAndCustomPaginationToGetAllCoursesForProgramRequest() {
        Integer id = addCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl + "/{programId}/courses", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("numberOfElements", equalTo(5));
    }

    @Test
    void shouldRespondWith401ToDeleteProgramRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/{programId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteProgramRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/{programId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteProgram() {
        Integer id = addPrograms();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{programId}", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{programId}", id)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith404IfCourseToDeleteNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{programId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToGetProgramRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{programId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithProgram() {
        Integer id = addPrograms();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{programId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Cognitive Science"));
    }

    @Test
    void shouldRespondWith404IfProgramNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{programId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToUpdateProgramRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .put(baseUrl + "/{programId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToUpdateProgramRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .put(baseUrl + "/{programId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithUpdatedProgram() {
        Integer id = addPrograms();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .put(baseUrl + "/{programId}", id)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(id))
                .body("name", equalTo("Cognitive Science"));

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{programId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Cognitive Science"));
    }
}
