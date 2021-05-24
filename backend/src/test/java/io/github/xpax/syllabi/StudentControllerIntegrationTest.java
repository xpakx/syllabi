package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.*;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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

    private Integer addStudyGroup() {
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
        courseYearRepository.save(year);

        List<Student> students = new ArrayList<>();
        List<User> users =  new ArrayList<>();
        for(int i = 5; i<30; i++) {
            User user = User.builder()
                    .username("user"+i)
                    .password("password")
                    .roles(new HashSet<>())
                    .build();
            users.add(user);
            Student student = Student.builder()
                    .name("Student"+i)
                    .surname("Student")
                    .user(user)
                    .build();
            students.add(student);
        }

        userRepository.saveAll(users);
        studentRepository.saveAll(students);

        StudyGroup group = StudyGroup.builder()
                .year(year)
                .students(new HashSet<>(students))
                .build();

        return studyGroupRepository.save(group).getId();
    }

    private Integer addCourseYear() {
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

        List<Student> students = new ArrayList<>();
        List<User> users =  new ArrayList<>();
        for(int i = 5; i<30; i++) {
            User user = User.builder()
                    .username("user"+i)
                    .password("password")
                    .roles(new HashSet<>())
                    .build();
            users.add(user);
            Student student = Student.builder()
                    .name("Student"+i)
                    .surname("Student")
                    .user(user)
                    .build();
            students.add(student);
        }

        userRepository.saveAll(users);
        studentRepository.saveAll(students);

        StudyGroup group = StudyGroup.builder()
                .year(year)
                .students(new HashSet<>(students))
                .build();

        studyGroupRepository.save(group);

        return yearId;
    }

    private void addStudents() {
        List<Student> students = new ArrayList<>();
        List<User> users =  new ArrayList<>();
        for(int i = 5; i<30; i++) {
            User user = User.builder()
                    .username("user"+i)
                    .password("password")
                    .roles(new HashSet<>())
                    .build();
            users.add(user);
            Student student = Student.builder()
                    .name("Student"+i)
                    .surname("Student")
                    .user(user)
                    .build();
            students.add(student);
        }

        userRepository.saveAll(users);
        studentRepository.saveAll(students);
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

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/student", id)
                .then()
                .statusCode(OK.value())
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

    @Test
    void shouldRespondWith401ToGetStudentRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/users/{userId}/student", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithStudent() {
        Integer id = addStudent();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/student", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("John"))
                .body("surname", equalTo("Agricola"));
    }

    @Test
    void shouldRespondWith404IfStudentNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/student", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToDeleteStudentRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/users/{userId}/student", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteStudentRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/users/{userId}/student", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteStudent() {
        Integer id = addStudent();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/users/{userId}/student", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/users/{userId}/student", id)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToUpdateStudentRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .put(baseUrl + "/users/{userId}/student", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToUpdateStudentRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(addStudentRequest)
                .when()
                .put(baseUrl + "/users/{userId}/student", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithUpdatedStudent() {
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
                .put(baseUrl + "/users/{userId}/student", id)
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
                .get(baseUrl + "/users/{userId}/student", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Adam"))
                .body("surname", equalTo("Smith"));
    }

    @Test
    void shouldRespondWith401ToGetAllStudentsForStudyGroupRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/groups/{groupId}/students", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToGetAllStudentsForStudyGroupRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/groups/{groupId}/students", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithStudentsPageAndDefaultPaginationToGetAllStudentsForStudyGroupRequest() {
        Integer id = addStudyGroup();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin2"))
                .when()
                .get(baseUrl + "/groups/{groupId}/students", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithStudentsPageAndCustomPaginationToGetAllStudentsForStudyGroupRequest() {
        Integer id = addStudyGroup();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin2"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl+ "/groups/{groupId}/students", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("numberOfElements", equalTo(5));
    }

    @Test
    void shouldRespondWith401ToGetAllStudentsForCourseYearRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/years/{yearId}/students", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToGetAllStudentsForCourseYearRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/years/{yearId}/students", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithStudentsPageAndDefaultPaginationToGetAllStudentsForCourseYearRequest() {
        Integer id = addCourseYear();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin2"))
                .when()
                .get(baseUrl + "/years/{yearId}/students", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithStudentsPageAndCustomPaginationToGetAllStudentsForCourseYearRequest() {
        Integer id = addCourseYear();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin2"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl+ "/years/{yearId}/students", id)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("numberOfElements", equalTo(5));
    }

    @Test
    void shouldRespondWith401ToGetAllStudentsRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/students")
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToGetAllStudentsRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/students")
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithStudentsPageAndDefaultPaginationToGetAllStudentsRequest() {
        addStudents();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .get(baseUrl + "/students")
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithStudentsPageAndCustomPaginationToGetAllStudentsRequest() {
        addStudents();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl+ "/students")
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("numberOfElements", equalTo(5));
    }
}
