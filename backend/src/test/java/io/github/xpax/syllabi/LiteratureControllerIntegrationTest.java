package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.repo.CourseLiteratureRepository;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.GroupLiteratureRepository;
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
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LiteratureControllerIntegrationTest {

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
    CourseLiteratureRepository courseLiteratureRepository;
    @Autowired
    GroupLiteratureRepository groupLiteratureRepository;

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
        courseLiteratureRepository.deleteAll();
        groupLiteratureRepository.deleteAll();
        courseRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addCourseLiteratureAndReturnCourseId() {
        Course course = Course.builder()
                .name("Philosophy of Science")
                .programs(new HashSet<>())
                .build();
        Integer courseId = courseRepository.save(course).getId();

        CourseLiterature lit1 = CourseLiterature.builder()
                .course(course)
                .title("The Aim and Structure of Physical Theory")
                .author("Pierre Duhem")
                .build();
        courseLiteratureRepository.save(lit1);
        CourseLiterature lit2 = CourseLiterature.builder()
                .course(course)
                .title("The Logic of Scientific Discovery")
                .author("Karl Popper")
                .build();
        courseLiteratureRepository.save(lit2);
        CourseLiterature lit3 = CourseLiterature.builder()
                .course(course)
                .title("The Structure of Scientific Revolutions")
                .author("Thomas Kuhn")
                .build();
        courseLiteratureRepository.save(lit3);

        for(int i = 4; i<=25; i++) {
            CourseLiterature dummyLiterature = CourseLiterature.builder()
                    .title("Dummy Literature #"+i)
                    .course(course)
                    .build();
            courseLiteratureRepository.save(dummyLiterature);
        }

        return courseId;
    }

    @Test
    void shouldRespondWith401ToGetCourseLiteratureRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/courses/{courseId}/literature", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithLiteraturePageAndDefaultPaginationToGetCourseLiteratureRequest() {
        Integer id = addCourseLiteratureAndReturnCourseId();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/courses/{courseId}/literature", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("content[0].title", equalTo("The Aim and Structure of Physical Theory"))
                .body("content[0].author", equalTo("Pierre Duhem"))
                .body("content[1].title", equalTo("The Logic of Scientific Discovery"))
                .body("content[1].author", equalTo("Karl Popper"))
                .body("content[2].title", equalTo("The Structure of Scientific Revolutions"))
                .body("content[2].author", equalTo("Thomas Kuhn"))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithLiteraturePageAndCustomPaginationToGetAllCourseLiteratureRequest() {
        Integer id = addCourseLiteratureAndReturnCourseId();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl + "/courses/{courseId}/literature", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("content[0].title", equalTo("Dummy Literature #16"))
                .body("numberOfElements", equalTo(5));
    }
}
