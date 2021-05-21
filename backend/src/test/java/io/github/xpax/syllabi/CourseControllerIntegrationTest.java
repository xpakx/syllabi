package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.UserRepository;
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
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        courseRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addCourses() {
        Course course1 = Course.builder()
                .name("Introduction to Cognitive Science")
                .programs(new HashSet<>())
                .build();
        courseRepository.save(course1);
        Course course2 = Course.builder()
                .name("Pragmatics")
                .programs(new HashSet<>())
                .build();
        int courseId = courseRepository.save(course2).getId();
        Course course3 = Course.builder()
                .name("Logic I")
                .programs(new HashSet<>())
                .build();
        courseRepository.save(course3);

        for (int i = 4; i <= 25; i++) {
            Course dummyCourse = Course.builder()
                    .name("Dummy Course #" + i)
                    .programs(new HashSet<>())
                    .build();
            courseRepository.save(dummyCourse);
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
}