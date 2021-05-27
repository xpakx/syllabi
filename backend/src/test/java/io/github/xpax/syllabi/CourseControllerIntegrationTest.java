package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.CourseRequest;
import io.github.xpax.syllabi.entity.dto.UpdateCourseRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.CourseYearRepository;
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

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CourseControllerIntegrationTest {

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
    CourseYearRepository courseYearRepository;

    private CourseRequest courseRequest;
    private UpdateCourseRequest updateCourseRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port + "").concat("/courses");
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

        courseRequest = new CourseRequest();
        courseRequest.setName("Added Course");

        updateCourseRequest = new UpdateCourseRequest();
        updateCourseRequest.setName("Edited Course");
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        courseYearRepository.deleteAll();
        courseRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addCourses() {
        Course course1 = Course.builder()
                .name("Introduction to Cognitive Science")
                .semesters(new HashSet<>())
                .build();
        courseRepository.save(course1);
        Course course2 = Course.builder()
                .name("Pragmatics")
                .semesters(new HashSet<>())
                .build();
        int courseId = courseRepository.save(course2).getId();
        Course course3 = Course.builder()
                .name("Logic I")
                .semesters(new HashSet<>())
                .build();
        courseRepository.save(course3);

        for (int i = 4; i <= 25; i++) {
            Course dummyCourse = Course.builder()
                    .name("Dummy Course #" + i)
                    .semesters(new HashSet<>())
                    .build();
            courseRepository.save(dummyCourse);
        }

        return courseId;
    }

    private Integer addCourseAndYears() {
        Course course = Course.builder()
                .name("Introduction to Cognitive Science")
                .semesters(new HashSet<>())
                .build();
        Integer courseId = courseRepository.save(course).getId();

        Calendar calendarActive = Calendar.getInstance();
        calendarActive.add(Calendar.YEAR, 1);

        Calendar calendarNotActive = Calendar.getInstance();
        calendarNotActive.add(Calendar.YEAR, -1);

        for(int i = 1; i<=50; i++) {
            CourseYear dummyCourseYear = CourseYear.builder()
                    .parent(course)
                    .description("Dummy Course Year #"+i)
                    .endDate(i%2==0 ? calendarActive.getTime() : calendarNotActive.getTime())
                    .build();
            courseYearRepository.save(dummyCourseYear);
        }
        return courseId;
    }

    @Test
    void shouldRespondWith401ToGetAllCoursesRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCoursesPageAndDefaultPaginationToGetAllCoursesRequest() {
        addCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("content[0].name", equalTo("Introduction to Cognitive Science"))
                .body("content[1].name", equalTo("Pragmatics"))
                .body("content[2].name", equalTo("Logic I"))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithCoursesPageAndCustomPaginationToGetAllCoursesRequest() {
        addCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("content[0].name", equalTo("Dummy Course #16"))
                .body("numberOfElements", equalTo(5));
    }

    @Test
    void shouldRespondWith401ToGetCourseRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{courseId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCourse() {
        Integer id = addCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Pragmatics"));
    }

    @Test
    void shouldRespondWith404IfCourseNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToAddCourseRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .post(baseUrl)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToAddCourseRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(courseRequest)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithAddedCourse() {
        addCourses();
        Integer addedCourseId = given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(courseRequest)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(CREATED.value())
                .body("name", equalTo("Added Course"))
                .extract()
                .jsonPath()
                .getInt("id");

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}", addedCourseId)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Added Course"));
    }

    @Test
    void shouldRespondWith401ToUpdateCourseRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .put(baseUrl + "/{courseId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToUpdateCourseRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(updateCourseRequest)
                .when()
                .put(baseUrl + "/{courseId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithUpdatedCourse() {
        Integer id = addCourses();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(updateCourseRequest)
                .when()
                .put(baseUrl + "/{courseId}", id)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(id))
                .body("name", equalTo("Edited Course"));

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Edited Course"));
    }

    @Test
    void shouldRespondWith401ToDeleteCourseRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/{courseId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteCourseRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/{courseId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteCourse() {
        Integer id = addCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{courseId}", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}", id)
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
                .delete(baseUrl + "/{courseId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToGetCourseYearsRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{courseId}/years", 1)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCourseYearsPageAndDefaultPaginationToGetCourseYearsRequest() {
        Integer id = addCourseAndYears();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}/years", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("content[0].description", equalTo("Dummy Course Year #1"))
                .body("content[0].parent.name", equalTo("Introduction to Cognitive Science"))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithCourseYearsPageAndCustomPaginationToGetCourseYearsRequest() {
        Integer id = addCourseAndYears();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl + "/{courseId}/years", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("content[0].description", equalTo("Dummy Course Year #16"))
                .body("content[0].parent.name", equalTo("Introduction to Cognitive Science"))
                .body("numberOfElements", equalTo(5));
    }

    @Test
    void shouldRespondWith401ToGetActiveCourseYearsRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{courseId}/years/active", 1)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCourseYearsPageAndDefaultPaginationToGetActiveCourseYearsRequest() {
        Integer id = addCourseAndYears();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}/years/active", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("content[0].description", equalTo("Dummy Course Year #2"))
                .body("content[0].parent.name", equalTo("Introduction to Cognitive Science"))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithCourseYearsPageAndCustomPaginationToGetActiveCourseYearsRequest() {
        Integer id = addCourseAndYears();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl + "/{courseId}/years/active", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("content[0].description", equalTo("Dummy Course Year #32"))
                .body("content[0].parent.name", equalTo("Introduction to Cognitive Science"))
                .body("numberOfElements", equalTo(5));
    }

    @Test
    void shouldRespondWith401ToGetCourseMinRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{courseId}/min", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCourseMin() {
        Integer id = addCourses();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}/min", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Pragmatics"));
    }

    @Test
    void shouldRespondWith404IfCourseMinNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{courseId}/min", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }
}