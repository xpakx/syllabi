package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Student;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.StudentWithUserId;
import io.github.xpax.syllabi.entity.dto.UpdateStudentRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
    private UpdateStudentRequest updateStudentRequest;
    private Page<StudentWithUserId> studentPage;

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

        updateStudentRequest = new UpdateStudentRequest();
        updateStudentRequest.setName("John");
        updateStudentRequest.setSurname("Smith");

        User user1 = User.builder()
                .id(5)
                .build();
        Student student1 = Student.builder()
                .name("John")
                .surname("Smith")
                .id(1)
                .user(user1)
                .build();
        StudentWithUserId student1Proj = factory.createProjection(StudentWithUserId.class, student1);

        List<StudentWithUserId> studentList = new ArrayList<>();
        studentList.add(student1Proj);
        studentPage = new PageImpl<>(studentList);
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

    @Test
    void shouldRespondToDeleteStudentRequest() {
        injectMocks();
        given()
                .when()
                .delete("/users/{userId}/student", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteStudent() {
        injectMocks();
        given()
                .when()
                .delete("/users/{userId}/student", 5)
                .then()
                .statusCode(OK.value());
        BDDMockito.then(studentService)
                .should(times(1))
                .deleteStudent(5);
    }

    @Test
    void shouldRespondToUpdateStudentRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateStudentRequest)
                .when()
                .put("/users/{userId}/student", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateStudent() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateStudentRequest)
                .when()
                .put("/users/{userId}/student", 5)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<UpdateStudentRequest> requestCaptor = ArgumentCaptor.forClass(UpdateStudentRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(studentService)
                .should(times(1))
                .updateStudent(requestCaptor.capture(), idCaptor.capture());
        UpdateStudentRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(5, id);
        assertEquals("John", request.getName());
        assertEquals("Smith", request.getSurname());
    }

    @Test
    void shouldProduceUpdatedStudent() {
        BDDMockito.given(studentService.updateStudent(any(UpdateStudentRequest.class), anyInt()))
                .willReturn(createdStudent);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateStudentRequest)
                .when()
                .put("/users/{userId}/student", 5)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("John"))
                .body("surname", equalTo("Smith"));
    }

    @Test
    void shouldRespondToGetStudentsForGroupRequest() {
        injectMocks();
        given()
                .when()
                .get("/groups/{groupId}/students", 13)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetStudentsForGroupRequest() {
        injectMocks();
        given()
                .queryParam("page", 2)
                .queryParam("size", 15)
                .when()
                .get("/groups/{groupId}/students", 13)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(studentService)
                .should(times(1))
                .getGroupStudents(13, 2, 15);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetStudentsForGroupRequest() {
        injectMocks();
        given()
                .when()
                .get("/groups/{groupId}/students", 13)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(studentService)
                .should(times(1))
                .getGroupStudents(13, 0, 20);
    }

    @Test
    void shouldProducePageOfStudentsForGroup() {
        BDDMockito.given(studentService.getGroupStudents(anyInt(), anyInt(), anyInt()))
                .willReturn(studentPage);
        injectMocks();
        given()
                .when()
                .get("/groups/{groupId}/students", 13)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(1))
                .body("content[0].id", equalTo(1))
                .body("content[0].user.id", equalTo(5))
                .body("content[0].name", equalTo("John"))
                .body("content[0].surname", equalTo("Smith"))
                .body("numberOfElements", equalTo(1));
    }

    @Test
    void shouldRespondToGetStudentsForYearRequest() {
        injectMocks();
        given()
                .when()
                .get("/years/{groupId}/students", 13)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetStudentsForYearRequest() {
        injectMocks();
        given()
                .queryParam("page", 2)
                .queryParam("size", 15)
                .when()
                .get("/years/{groupId}/students", 13)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(studentService)
                .should(times(1))
                .getStudents(13, 2, 15);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetStudentsForYearRequest() {
        injectMocks();
        given()
                .when()
                .get("/years/{groupId}/students", 13)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(studentService)
                .should(times(1))
                .getStudents(13, 0, 20);
    }

    @Test
    void shouldProducePageOfStudentsFromYear() {
        BDDMockito.given(studentService.getStudents(anyInt(), anyInt(), anyInt()))
                .willReturn(studentPage);
        injectMocks();
        given()
                .when()
                .get("/years/{groupId}/students", 13)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(1))
                .body("content[0].id", equalTo(1))
                .body("content[0].user.id", equalTo(5))
                .body("content[0].name", equalTo("John"))
                .body("content[0].surname", equalTo("Smith"))
                .body("numberOfElements", equalTo(1));
    }
}
