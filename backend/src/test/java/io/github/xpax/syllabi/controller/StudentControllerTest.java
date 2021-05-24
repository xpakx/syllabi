package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.StudentWithUserId;
import io.github.xpax.syllabi.entity.dto.UserToStudentRequest;
import io.github.xpax.syllabi.error.StudentExistsException;
import io.github.xpax.syllabi.service.StudentService;
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
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class StudentControllerTest {
    @Mock
    private StudentService studentService;

    private UserToStudentRequest createStudentRequest;
    private Student createdStudent;
    private StudentWithUserId createdStudentProj;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        createStudentRequest = new UserToStudentRequest();
        createStudentRequest.setName("John");
        createStudentRequest.setSurname("Smith");

        User user = User.builder()
                .id(5)
                .build();
        createdStudent = Student.builder()
                .id(1)
                .user(user)
                .name("John")
                .surname("Smith")
                .build();
        createdStudentProj = factory.createProjection(StudentWithUserId.class, createdStudent);
    }

    private void injectMocks() {
        StudentController studentController = new StudentController(studentService);
        RestAssuredMockMvc.standaloneSetup(studentController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToCreateStudentRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("/users/{userId}/student", 5)
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldCreateStudent() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("/users/{userId}/student", 5)
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<UserToStudentRequest> requestCaptor = ArgumentCaptor.forClass(UserToStudentRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(studentService)
                .should(times(1))
                .createStudent(requestCaptor.capture(), idCaptor.capture());
        UserToStudentRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(5, id);
        assertEquals("John", request.getName());
        assertEquals("Smith", request.getSurname());
    }

    @Test
    void shouldProduceCreatedStudent() {
        BDDMockito.given(studentService.createStudent(any(UserToStudentRequest.class), anyInt()))
                .willReturn(createdStudent);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("/users/{userId}/student", 5)
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(1))
                .body("name", equalTo("John"))
                .body("surname", equalTo("Smith"));
    }

    @Test
    void shouldNotCreateStudentIfStudentForThisUserAlreadyExist() {
        BDDMockito.given(studentService.createStudent(any(UserToStudentRequest.class), anyInt()))
                .willThrow(StudentExistsException.class);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(createStudentRequest)
                .when()
                .post("/users/{userId}/student", 5)
                .then()
                .statusCode(BAD_REQUEST.value());
    }

    @Test
    void shouldRespondToGetStudentRequest() {
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/student", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceStudent() {
        BDDMockito.given(studentService.getStudent(5))
                .willReturn(createdStudentProj);
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/student", 5)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("John"))
                .body("surname", equalTo("Smith"))
                .body("user.id", equalTo(5));
    }
}
