package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Job;
import io.github.xpax.syllabi.entity.Teacher;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.*;
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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class TeacherControllerTest {
    @Mock
    private TeacherService teacherService;

    private UserToTeacherRequest createTeacherRequest;
    private Teacher createdTeacher;
    private TeacherDetails createdTeacherDet;
    private UpdateTeacherRequest updateTeacherRequest;
    private UpdateJobRequest updateJobRequest;
    private Job updatedJob;
    private JobSummary updatedJobSummary;
    private Page<TeacherSummary> teacherPage;

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
        this.createdTeacherDet = factory.createProjection(TeacherDetails.class, createdTeacher);

        updateTeacherRequest = new UpdateTeacherRequest();
        updateTeacherRequest.setName("John");
        updateTeacherRequest.setSurname("Smith");

        updatedJob = Job.builder()
                .id(1)
                .name("Researcher")
                .build();
        updateJobRequest = new UpdateJobRequest();
        updateJobRequest.setName("Researcher");

        updatedJobSummary = factory.createProjection(JobSummary.class, updatedJob);


        Teacher teacher1 = Teacher.builder()
                .id(0)
                .name("John")
                .build();
        Teacher teacher2 = Teacher.builder()
                .id(1)
                .name("George")
                .build();
        Teacher teacher3 = Teacher.builder()
                .id(2)
                .name("Michael")
                .build();

        TeacherSummary teacher1Det = factory.createProjection(TeacherSummary.class, teacher1);
        TeacherSummary teacher2Det = factory.createProjection(TeacherSummary.class, teacher2);
        TeacherSummary teacher3Det = factory.createProjection(TeacherSummary.class, teacher3);
        List<TeacherSummary> teacherList = new ArrayList<>();
        teacherList.add(teacher1Det);
        teacherList.add(teacher2Det);
        teacherList.add(teacher3Det);
        teacherPage = new PageImpl<>(teacherList);

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

    @Test
    void shouldRespondToGetTeacherRequest() {
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/teacher", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceTeacher() {
        BDDMockito.given(teacherService.getTeacher(5))
                .willReturn(createdTeacherDet);
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/teacher", 5)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("John"))
                .body("surname", equalTo("Smith"));
    }

    @Test
    void shouldRespondToDeleteTeacherRequest() {
        injectMocks();
        given()
                .when()
                .delete("/users/{userId}/teacher", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteTeacher() {
        injectMocks();
        given()
                .when()
                .delete("/users/{userId}/teacher", 5)
                .then()
                .statusCode(OK.value());
        BDDMockito.then(teacherService)
                .should(times(1))
                .deleteTeacher(5);
    }

    @Test
    void shouldRespondToUpdateTeacherRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateTeacherRequest)
                .when()
                .put("/users/{userId}/teacher", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateTeacher() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateTeacherRequest)
                .when()
                .put("/users/{userId}/teacher", 5)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<UpdateTeacherRequest> requestCaptor = ArgumentCaptor.forClass(UpdateTeacherRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(teacherService)
                .should(times(1))
                .updateTeacher(requestCaptor.capture(), idCaptor.capture());
        UpdateTeacherRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(5, id);
        assertEquals("John", request.getName());
        assertEquals("Smith", request.getSurname());
    }

    @Test
    void shouldProduceUpdatedTeacher() {
        BDDMockito.given(teacherService.updateTeacher(any(UpdateTeacherRequest.class), anyInt()))
                .willReturn(createdTeacher);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateTeacherRequest)
                .when()
                .put("/users/{userId}/teacher", 5)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("John"))
                .body("surname", equalTo("Smith"));
    }

    @Test
    void shouldRespondToUpdateJobRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateJobRequest)
                .when()
                .put("/users/{userId}/teacher/job", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateJob() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateJobRequest)
                .when()
                .put("/users/{userId}/teacher/job", 5)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<UpdateJobRequest> requestCaptor = ArgumentCaptor.forClass(UpdateJobRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(teacherService)
                .should(times(1))
                .updateTeacherJob(requestCaptor.capture(), idCaptor.capture());
        UpdateJobRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(5, id);
        assertEquals("Researcher", request.getName());
    }

    @Test
    void shouldProduceUpdatedJob() {
        BDDMockito.given(teacherService.updateTeacherJob(any(UpdateJobRequest.class), anyInt()))
                .willReturn(updatedJob);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateTeacherRequest)
                .when()
                .put("/users/{userId}/teacher/job", 5)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("Researcher"));
    }

    @Test
    void shouldRespondToGetTeacherJobRequest() {
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/teacher/job", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceTeacherJob() {
        BDDMockito.given(teacherService.getTeacherJob(5))
                .willReturn(updatedJobSummary);
        injectMocks();
        given()
                .when()
                .get("/users/{userId}/teacher/job", 5)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("Researcher"));
    }

    @Test
    void shouldRespondToAllRequest() {
        injectMocks();
        given()
                .when()
                .get("/teachers")
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceListOfCourses() {
        BDDMockito.given(teacherService.getTeachers(anyInt(), anyInt()))
                .willReturn(teacherPage);
        injectMocks();
        given()
                .when()
                .get("/teachers")
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(3))
                .body("content[0].id", equalTo(0))
                .body("content[0].name", equalTo("John"))
                .body("content[1].id", equalTo(1))
                .body("content[1].name", equalTo("George"))
                .body("content[2].id", equalTo(2))
                .body("content[2].name", equalTo("Michael"))
                .body("numberOfElements", equalTo(3));
    }

    @Test
    void shouldTakePageAndSizeFromGetCoursesRequest() {
        injectMocks();
        given()
                .queryParam("page", 7)
                .queryParam("size", 5)
                .when()
                .get("/teachers")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(teacherService)
                .should(times(1))
                .getTeachers(7, 5);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForCoursesRequest() {
        injectMocks();
        given()
                .when()
                .get("/teachers")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(teacherService)
                .should(times(1))
                .getTeachers(0, 20);
    }
}
