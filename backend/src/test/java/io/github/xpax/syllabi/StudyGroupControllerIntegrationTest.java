package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.*;
import io.github.xpax.syllabi.entity.dto.StudyGroupRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.CourseYearRepository;
import io.github.xpax.syllabi.repo.StudyGroupRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import io.github.xpax.syllabi.security.JwtTokenUtil;
import io.github.xpax.syllabi.service.UserService;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudyGroupControllerIntegrationTest {

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
    StudyGroupRepository studyGroupRepository;
    @Autowired
    CourseYearRepository courseYearRepository;
    @Autowired
    CourseRepository courseRepository;

    private StudyGroupRequest groupRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port+"").concat("/groups");
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
        studyGroupRepository.deleteAll();
        courseYearRepository.deleteAll();
        courseRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addGroup() {
        Course course = Course.builder()
                .name("Introduction to Linguistics")
                .programs(new HashSet<>())
                .build();
        courseRepository.save(course);

        CourseYear year = CourseYear.builder()
                .parent(course)
                .description("First Year")
                .coordinatedBy(new HashSet<>())
                .build();
        Integer yearId = courseYearRepository.save(year).getId();

        StudyGroup group = StudyGroup.builder()
                .year(year)
                .description("First Group")
                .build();

        groupRequest = new StudyGroupRequest();
        groupRequest.setDescription("Edited Group");

        return studyGroupRepository.save(group).getId();
    }

    @Test
    void shouldRespondWith401ToGetStudyGroupRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{groupId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithGroup() {
        Integer id = addGroup();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{groupId}", id)
                .then()
                .statusCode(OK.value())
                .body("description", equalTo("First Group"));
    }

    @Test
    void shouldRespondWith404IfGroupNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{groupId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }
}
