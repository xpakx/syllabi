package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.entity.dto.CourseForPage;
import io.github.xpax.syllabi.entity.dto.SemesterRequest;
import io.github.xpax.syllabi.service.CourseService;
import io.github.xpax.syllabi.service.SemesterService;
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
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class SemesterControllerTest {
    @Mock
    private SemesterService semesterService;
    @Mock
    private CourseService courseService;

    private Semester semester;
    private SemesterRequest semesterRequest;
    private Page<CourseForPage> coursePage;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        semester = Semester.builder()
                .name("Semester")
                .id(1)
                .number(1)
                .build();

        semesterRequest = new SemesterRequest();
        semesterRequest.setName("Semester");
        semesterRequest.setNumber(1);

        Course course = Course.builder()
                .id(3)
                .name("Introduction to Archeology")
                .build();
        CourseForPage courseForPage = factory.createProjection(CourseForPage.class, course);
        List<CourseForPage> courseList = new ArrayList<>();
        courseList.add(courseForPage);
        coursePage = new PageImpl<>(courseList);
    }

    private void injectMocks() {
        SemesterController controller = new SemesterController(semesterService, courseService);
        RestAssuredMockMvc.standaloneSetup(controller);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToGetSemesterRequest() {
        injectMocks();
        given()
                .when()
                .get("/semesters/{semesterId}", 1)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceSemester() {
        BDDMockito.given(semesterService.getSemester(1))
                .willReturn(semester);
        injectMocks();
        given()
                .when()
                .get("/semesters/{semesterId}", 1)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("number", equalTo(1))
                .body("name", equalTo("Semester"));
    }

    @Test
    void shouldRespondToDeleteRequest() {
        injectMocks();
        given()
                .when()
                .delete("/semesters/{semesterId}", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteSemester() {
        injectMocks();
        given()
                .when()
                .delete("/semesters/{semesterId}", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(semesterService)
                .should(times(1))
                .deleteSemester(5);
    }

    @Test
    void shouldRespondToUpdateSemesterRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(semesterRequest)
                .when()
                .put("/semesters/{semesterId}", 1)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateSemester() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(semesterRequest)
                .when()
                .put("/semesters/{semesterId}", 1)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<SemesterRequest> requestCaptor = ArgumentCaptor.forClass(SemesterRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(semesterService)
                .should(times(1))
                .updateSemester(requestCaptor.capture(), idCaptor.capture());
        SemesterRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(1, id);
        assertEquals("Semester", request.getName());
        assertEquals(1, request.getNumber());
    }

    @Test
    void shouldProduceUpdatedSemester() {
        BDDMockito.given(semesterService.updateSemester(any(SemesterRequest.class), anyInt()))
                .willReturn(semester);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(semesterRequest)
                .when()
                .put("/semesters/{semesterId}", 1)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("Semester"));
    }

    @Test
    void shouldRespondToGetAllCoursesRequest() {
        injectMocks();
        given()
                .when()
                .get("semesters/{semesterId}/courses", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetAllCoursesRequest() {
        injectMocks();
        given()
                .queryParam("page", 2)
                .queryParam("size", 15)
                .when()
                .get("semesters/{semesterId}/courses", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getAllCoursesBySemesterId(2, 15, 5);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetAllCoursesRequest() {
        injectMocks();
        given()
                .when()
                .get("semesters/{semesterId}/courses", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getAllCoursesBySemesterId(0, 20, 5);
    }

    @Test
    void shouldProducePageOfCourses() {
        BDDMockito.given(courseService.getAllCoursesBySemesterId(anyInt(), anyInt(), anyInt()))
                .willReturn(coursePage);
        injectMocks();
        given()
                .when()
                .get("semesters/{semesterId}/courses", 5)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(1))
                .body("content[0].id", equalTo(3))
                .body("content[0].name", equalTo("Introduction to Archeology"))
                .body("numberOfElements", equalTo(1));
    }
}
