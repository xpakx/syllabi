package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.ChangePasswordRequest;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.entity.dto.RoleRequest;
import io.github.xpax.syllabi.entity.dto.UserWithoutPassword;
import io.github.xpax.syllabi.service.CourseService;
import io.github.xpax.syllabi.service.UserAccountService;
import io.github.xpax.syllabi.service.UserRoleService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.MockMvcConfig;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @Mock
    private UserRoleService userRoleService;
    @Mock
    private UserAccountService userAccountService;
    @Mock
    private CourseService courseService;

    private RoleRequest roleRequest;
    private User userWithAddedRole;
    private Page<UserWithoutPassword> userPage;
    private UserWithoutPassword user;
    private ChangePasswordRequest changePasswordRequest;
    private Page<CourseForPage> coursePage;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        roleRequest = new RoleRequest();
        roleRequest.setRole("ADMIN");

        Role role = new Role();
        role.setId(0);
        role.setAuthority(Role.COURSE_ADMIN);
        Set<Role> roleList = new HashSet<>();
        roleList.add(role);
        userWithAddedRole = User.builder()
                .id(3)
                .username("username")
                .roles(roleList)
                .build();
        List<UserWithoutPassword> userList = new ArrayList<>();
        userList.add(factory.createProjection(UserWithoutPassword.class, userWithAddedRole));
        userPage = new PageImpl<>(userList);

        User user = User.builder()
                .id(3)
                .username("user1")
                .build();
        this.user = factory.createProjection(UserWithoutPassword.class, user);

        changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setPassword("password");
        changePasswordRequest.setPasswordRe("password");
        changePasswordRequest.setPasswordOld("oldPassword");

        Course course1 = Course.builder()
                .id(1)
                .name("Neuroanatomy")
                .build();
        Course course2 = Course.builder()
                .id(2)
                .name("Introduction to Cognitive Science")
                .build();

        CourseForPage courseForPage1 = factory.createProjection(CourseForPage.class, course1);
        CourseForPage courseForPage2 = factory.createProjection(CourseForPage.class, course2);

        List<CourseForPage> courseList = new ArrayList<>();
        courseList.add(courseForPage1);
        courseList.add(courseForPage2);
        coursePage = new PageImpl<>(courseList);
    }

    private void injectMocks() {
        UserController userController = new UserController(userRoleService, userAccountService, courseService);
        RestAssuredMockMvc.standaloneSetup(userController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToAddRoleForUserRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(roleRequest)
                .when()
                .post("/users/{userId}/roles", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldAddRoleForUser() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(roleRequest)
                .when()
                .post("/users/{userId}/roles", 3)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<RoleRequest> requestCaptor = ArgumentCaptor.forClass(RoleRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(userRoleService)
                .should(times(1))
                .addRoleForUser(requestCaptor.capture(), idCaptor.capture());
        RoleRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(3, id);
        assertEquals("ADMIN", request.getRole());
    }

    @Test
    void shouldProduceUserAfterAddingRole() {
        BDDMockito.given(userRoleService.addRoleForUser(any(RoleRequest.class), anyInt()))
                .willReturn(userWithAddedRole);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(roleRequest)
                .when()
                .post("/users/{userId}/roles", 3)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(3))
                .body("username", equalTo("username"))
                .body("roles", hasSize(1))
                .body("roles[0].id", equalTo(0))
                .body("roles[0].authority", equalTo(Role.COURSE_ADMIN));
    }

    @Test
    void shouldRespondToGetAllUsersRequest() {
        injectMocks();
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetAllUsersRequest() {
        injectMocks();
        given()
                .queryParam("page", 2)
                .queryParam("size", 15)
                .when()
                .get("/users")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(userAccountService)
                .should(times(1))
                .getAllUsers(2, 15);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetAllUsersRequest() {
        injectMocks();
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(userAccountService)
                .should(times(1))
                .getAllUsers(0, 20);
    }

    @Test
    void shouldProducePageOfAllUsers() {
        BDDMockito.given(userAccountService.getAllUsers(anyInt(), anyInt()))
                .willReturn(userPage);
        injectMocks();
        given()
                .when()
                .get("/users")
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(1))
                .body("content[0].id", equalTo(3))
                .body("content[0].username", equalTo("username"))
                .body("numberOfElements", equalTo(1));
    }

    @Test
    void shouldRespondToDeleteUserRequest() {
        injectMocks();
        given()
                .when()
                .delete("/users/{userId}", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteUser() {
        injectMocks();
        given()
                .when()
                .delete("/users/{userId}", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(userAccountService)
                .should(times(1))
                .deleteUser(3);
    }

    @Test
    void shouldRespondToGetUserRequest() {
        injectMocks();
        given()
                .when()
                .get("/users/{userId}", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceUser() {
        BDDMockito.given(userAccountService.getUser(3))
                .willReturn(user);
        injectMocks();
        given()
                .when()
                .get("/users/{userId}", 3)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(3))
                .body("username", equalTo("user1"));
    }

    @Test
    void shouldRespondToChangePasswordRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(changePasswordRequest)
                .when()
                .put("/users/{userId}", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldChangePassword() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(changePasswordRequest)
                .when()
                .put("/users/{userId}", 3)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<ChangePasswordRequest> requestCaptor = ArgumentCaptor.forClass(ChangePasswordRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(userAccountService)
                .should(times(1))
                .changePassword(requestCaptor.capture(), idCaptor.capture());
        ChangePasswordRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(3, id);
        assertEquals("password", request.getPassword());
        assertEquals("password", request.getPasswordRe());
        assertEquals("oldPassword", request.getPasswordOld());
    }

    @Test
    void shouldRespondToGetUserCoursesRequest() {
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/courses", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetUserCoursesRequest() {
        injectMocks();
        given()
                .queryParam("page", 2)
                .queryParam("size", 15)
                .when()
                .get("/users/{userId}/courses", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getAllCoursesByUserId(2, 15, 3);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetUserCoursesRequest() {
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/courses", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getAllCoursesByUserId(0, 20, 3);
    }

    @Test
    void shouldProducePageOfUserCourses() {
        BDDMockito.given(courseService.getAllCoursesByUserId(anyInt(), anyInt(), anyInt()))
                .willReturn(coursePage);
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/courses", 3)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(2))
                .body("content[0].id", equalTo(1))
                .body("content[0].name", equalTo("Neuroanatomy"))
                .body("content[1].id", equalTo(2))
                .body("content[1].name", equalTo("Introduction to Cognitive Science"))
                .body("numberOfElements", equalTo(2));
    }
}
