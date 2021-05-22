package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.*;
import io.github.xpax.syllabi.entity.dto.CourseYearRequest;
import io.github.xpax.syllabi.repo.*;
import io.github.xpax.syllabi.security.JwtTokenUtil;
import io.github.xpax.syllabi.service.UserService;
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
class CourseYearControllerIntegrationTest {

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
    @Autowired
    TeacherRepository teacherRepository;

    @Autowired
    StudyGroupRepository studyGroupRepository;

    private CourseYearRequest updateCourseYearRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port + "").concat("/years");
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

        updateCourseYearRequest = new CourseYearRequest();
        updateCourseYearRequest.setDescription("Edited Year");
    }

    @AfterEach
    void cleanUp() {
        studyGroupRepository.deleteAll();
        courseYearRepository.deleteAll();
        courseRepository.deleteAll();
        teacherRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addCoursesAndYears() {
        Course linguisticsIntro = Course.builder()
                .name("Introduction to Linguistics")
                .programs(new HashSet<>())
                .build();
        courseRepository.save(linguisticsIntro);

        Course acousticPhonetics = Course.builder()
                .name("Acoustic Phonetics")
                .programs(new HashSet<>())
                .build();
        courseRepository.save(acousticPhonetics);

        Course languageEvolution = Course.builder()
                .name("Language Evolution")
                .programs(new HashSet<>())
                .build();
        courseRepository.save(languageEvolution);

        updateCourseYearRequest = new CourseYearRequest();
        updateCourseYearRequest.setDescription("Edited Year");

        User teacher1User = User.builder()
                .username("teacher1")
                .password("test")
                .roles(new HashSet<>())
                .build();
        userRepository.save(teacher1User);


        Teacher teacher1 = Teacher.builder()
                .name("John")
                .surname("Smith")
                .job(Job.builder()
                        .name("assistant")
                        .build())
                .user(teacher1User)
                .build();
        teacherRepository.save(teacher1);
        Teacher teacher2 = Teacher.builder()
                .name("Jane")
                .surname("Smith")
                .job(Job.builder()
                        .name("lecturer")
                        .build())
                .build();
        teacherRepository.save(teacher2);

        Set<Teacher> teacherSet = new HashSet<>();
        teacherSet.add(teacher1);
        teacherSet.add(teacher2);

        Calendar calendarActive = Calendar.getInstance();
        calendarActive.add(Calendar.YEAR, 1);

        Calendar calendarNotActive = Calendar.getInstance();
        calendarNotActive.add(Calendar.YEAR, -1);

        CourseYear year = CourseYear.builder()
                .parent(languageEvolution)
                .description("First Year")
                .coordinatedBy(teacherSet)
                .startDate(calendarNotActive.getTime())
                .endDate(calendarActive.getTime())
                .build();
        Integer yearId = courseYearRepository.save(year).getId();

        for (int i = 2; i <= 25; i++) {
            CourseYear dummyCourseYear = CourseYear.builder()
                    .parent(linguisticsIntro)
                    .description("Dummy Course Year #" + i)
                    .build();
            courseYearRepository.save(dummyCourseYear);
        }
        return yearId;
    }

    @Test
    void shouldRespondWith401ToGetCourseYearRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{yearId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithCourseYear() {
        Integer id = addCoursesAndYears();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{yearId}", id)
                .then()
                .statusCode(OK.value())
                .body("description", equalTo("First Year"))
                .body("parent.name", equalTo("Language Evolution"))
                .body("coordinatedBy", hasSize(2));
    }

    @Test
    void shouldRespondWith404IfCourseYearNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{yearId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToDeleteCourseYearRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/{yearId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteCourseYearRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/{yearId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteCourseYear() {
        Integer id = addCoursesAndYears();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{yearId}", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{yearId}", id)
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
                .delete(baseUrl + "/{yearId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }
}