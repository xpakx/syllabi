package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.UserToTeacherRequest;
import io.github.xpax.syllabi.service.TeacherService;
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
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class TeacherControllerTest {
    @Mock
    private TeacherService teacherService;

    private UserToTeacherRequest createTeacherRequest;
    private Teacher createdTeacher;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        createTeacherRequest = new UserToTeacherRequest();
        createTeacherRequest.setName("John");
        createTeacherRequest.setSurname("Smith");
        User user = User.builder()
                .id(5)
                .build();
        createdTeacher = Teacher.builder()
                .id(1)
                .user(user)
                .name("John")
                .surname("Smith")
                .build();
    }

    private void injectMocks() {
        TeacherController teacherController = new TeacherController(teacherService);
        RestAssuredMockMvc.standaloneSetup(teacherController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToCreateTeacherRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(createTeacherRequest)
                .when()
                .post("/users/{userId}/teacher", 5)
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldCreateTeacher() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(createTeacherRequest)
                .when()
                .post("/users/{userId}/teacher", 5)
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<UserToTeacherRequest> requestCaptor = ArgumentCaptor.forClass(UserToTeacherRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(teacherService)
                .should(times(1))
                .createTeacher(requestCaptor.capture(), idCaptor.capture());
        UserToTeacherRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(5, id);
        assertEquals("John", request.getName());
        assertEquals("Smith", request.getSurname());
    }

    @Test
    void shouldProduceCreatedTeacher() {
        BDDMockito.given(teacherService.createTeacher(any(UserToTeacherRequest.class), anyInt()))
                .willReturn(createdTeacher);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(createTeacherRequest)
                .when()
                .post("/users/{userId}/teacher", 5)
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(1))
                .body("name", equalTo("John"))
                .body("surname", equalTo("Smith"));
    }
}
