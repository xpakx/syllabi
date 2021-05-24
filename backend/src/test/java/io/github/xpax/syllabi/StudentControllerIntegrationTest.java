package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.UserToStudentRequest;
import io.github.xpax.syllabi.repo.*;
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
class StudentControllerIntegrationTest {
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
    StudentRepository studentRepository;

    private UserToStudentRequest addStudentRequest;

    @Autowired
    private StudyGroupRepository studyGroupRepository;
    @Autowired
    private CourseYearRepository courseYearRepository;
    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port+"");
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

        Role admin2Role = new Role();
        admin2Role.setAuthority("ROLE_COURSE_ADMIN");
        Set<Role> roles2 = new HashSet<>();
        roles2.add(admin2Role);
        User admin2 = User.builder()
                .username("admin2")
                .password("password")
                .roles(roles2)
                .build();
        userRepository.save(admin2);

        addStudentRequest = new UserToStudentRequest();
        addStudentRequest.setName("Adam");
        addStudentRequest.setSurname("Smith");
    }

    @AfterEach
    void cleanUp() {
        studyGroupRepository.deleteAll();
        courseYearRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
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

    private Integer addStudent() {
        User user = User.builder()
                .username("user2")
                .password("password")
                .roles(new HashSet<>())
                .build();
        user = userRepository.save(user);
        Student student = Student.builder()
                .name("John")
                .surname("Agricola")
                .user(user)
                .build();
        studentRepository.save(student);
        return user.getId();
    }

    @Test
    void shouldRespondWith401ToAddStudentRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .post(baseUrl + "/users/{userId}/student", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToAddStudentRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(addStudentRequest)
                .when()
                .post(baseUrl + "/users/{userId}/student", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithAddedStudent() {
        Integer id = addUser();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(addStudentRequest)
                .when()
                .post(baseUrl + "/users/{userId}/student", id)
                .then()
                .statusCode(CREATED.value())
                .body("name", equalTo("Adam"))
                .body("surname", equalTo("Smith"));
    }

    @Test
    void shouldNotCreateStudentIfStudentForThisUserAlreadyExist() {
        Integer id = addStudent();
        given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(addStudentRequest)
                .when()
                .post(baseUrl + "/users/{userId}/student", id)
                .then()
                .statusCode(BAD_REQUEST.value());
    }
}
