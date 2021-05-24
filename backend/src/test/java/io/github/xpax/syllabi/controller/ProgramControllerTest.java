package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.service.CourseService;
import io.github.xpax.syllabi.service.ProgramService;
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
public class ProgramControllerTest {
    @Mock
    private CourseService courseService;
    @Mock
    private ProgramService programService;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private ProgramRequest programRequest;
    private Program addedProgram;
    private Page<CourseForPage> coursePage;
    private ProgramDetails program;

    @BeforeEach
    void setUp() {
        programRequest = new ProgramRequest();
        programRequest.setName("Philosophy");
        programRequest.setDescription("Philosophy program");
        addedProgram = Program.builder()
                .id(17)
                .name("Philosophy")
                .description("Philosophy program")
                .build();

        Course course1 = Course.builder()
                .id(0)
                .name("Introduction to Cognitive Science")
                .build();
        Course course2 = Course.builder()
                .id(1)
                .name("Pragmatics")
                .build();
        Course course3 = Course.builder()
                .id(2)
                .name("Logic I")
                .build();

        CourseForPage courseCognitiveScience = factory.createProjection(CourseForPage.class, course1);
        CourseForPage coursePragmatics = factory.createProjection(CourseForPage.class, course2);
        CourseForPage courseLogic = factory.createProjection(CourseForPage.class, course3);
        List<CourseForPage> courseList = new ArrayList<>();
        courseList.add(courseCognitiveScience);
        courseList.add(coursePragmatics);
        courseList.add(courseLogic);
        coursePage = new PageImpl<>(courseList);

        program = factory.createProjection(ProgramDetails.class, addedProgram);
    }

    private void injectMocks() {
        ProgramController programController = new ProgramController(courseService, programService);
        RestAssuredMockMvc.standaloneSetup(programController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToAddNewProgramRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .post("/programs")
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldCreateNewProgram() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .post("/programs")
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<ProgramRequest> requestCaptor = ArgumentCaptor.forClass(ProgramRequest.class);
        BDDMockito.then(programService)
                .should(times(1))
                .addNewProgram(requestCaptor.capture());

        ProgramRequest capturedRequest = requestCaptor.getValue();
        assertEquals("Philosophy", capturedRequest.getName());
        assertEquals("Philosophy program", capturedRequest.getDescription());
    }

    @Test
    void shouldReturnAddedProgram() {
        BDDMockito.given(programService.addNewProgram(any(ProgramRequest.class)))
                .willReturn(addedProgram);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .post("/programs")
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(17))
                .body("name", equalTo("Philosophy"))
                .body("description", equalTo("Philosophy program"));
    }

    @Test
    void shouldRespondToCoursesForProgramRequest() {
        injectMocks();
        given()
                .when()
                .get("/programs/{programId}/courses", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceListOfCoursesForProgram() {
        BDDMockito.given(courseService.getAllCoursesByProgramId(anyInt(), anyInt(), anyInt()))
                .willReturn(coursePage);
        injectMocks();
        given()
                .when()
                .get("/programs/{programId}/courses", 5)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(3))
                .body("content[0].id", equalTo(0))
                .body("content[0].name", equalTo("Introduction to Cognitive Science"))
                .body("content[1].id", equalTo(1))
                .body("content[1].name", equalTo("Pragmatics"))
                .body("content[2].id", equalTo(2))
                .body("content[2].name", equalTo("Logic I"))
                .body("numberOfElements", equalTo(3));
    }

    @Test
    void shouldTakePageAndSizeFromGetCoursesForProgramRequest() {
        injectMocks();
        given()
                .queryParam("page", 7)
                .queryParam("size", 5)
                .when()
                .get("/programs/{programId}/courses", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getAllCoursesByProgramId(7, 5, 5);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForCoursesForProgramRequest() {
        injectMocks();
        given()
                .when()
                .get("/programs/{programId}/courses", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getAllCoursesByProgramId(0, 20, 5);
    }

    @Test
    void shouldRespondToDeleteRequest() {
        injectMocks();
        given()
                .when()
                .delete("/programs/{programId}", 9)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteProgram() {
        injectMocks();
        given()
                .when()
                .delete("/programs/{programId}", 9)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(programService)
                .should(times(1))
                .deleteProgram(9);
    }

    @Test
    void shouldRespondToGetProgramRequest() {
        injectMocks();
        given()
                .when()
                .get("/programs/{programId}", 17)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceCourse() {
        BDDMockito.given(programService.getProgram(17))
                .willReturn(program);
        injectMocks();
        given()
                .when()
                .get("/programs/{programId}", 17)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(17))
                .body("name", equalTo("Philosophy"));
    }

    @Test
    void shouldAskServiceForCourse() {
        injectMocks();
        given()
                .when()
                .get("/programs/{programId}", 17)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(programService)
                .should(times(1))
                .getProgram(17);
    }

    @Test
    void shouldRespondToUpdateCourseRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .put("/programs/{programId}", 17)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateCourse() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .put("/programs/{programId}", 17)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<ProgramRequest> requestCaptor = ArgumentCaptor.forClass(ProgramRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(programService)
                .should(times(1))
                .updateProgram(requestCaptor.capture(), idCaptor.capture());

        Integer id = idCaptor.getValue();
        assertEquals(17, id);

        ProgramRequest capturedRequest = requestCaptor.getValue();
        assertEquals("Philosophy", capturedRequest.getName());
        assertEquals("Philosophy program", capturedRequest.getDescription());
    }

    @Test
    void shouldReturnUpdatedCourse() {
        BDDMockito.given(programService.updateProgram(any(ProgramRequest.class), anyInt()))
                .willReturn(addedProgram);
        injectMocks();

        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .put("/programs/{programId}", 1)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(17))
                .body("name", equalTo("Philosophy"))
                .body("description", equalTo("Philosophy program"));
    }
}
