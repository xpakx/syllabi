package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Course;
import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.dto.*;
import io.github.xpax.syllabi.service.CourseService;
import io.github.xpax.syllabi.service.CourseYearService;
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
class CourseControllerTest {
    @Mock
    private CourseService courseService;
    @Mock
    private CourseYearService courseYearService;

    private Page<CourseForPage> coursePage;
    private CourseDetails course1;
    private NewCourseRequest newCourseRequest;
    private Course addedCourse;
    private UpdateCourseRequest updateCourseRequest;
    private Course updatedCourse;
    private CourseYearRequest yearRequest;
    private CourseYear addedCourseYear;
    private Page<CourseYearForPage> courseYearsPage;
    private CourseSummary course1Min;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();


    @BeforeEach
    void setUp() {
        Course course1 = Course.builder()
                .id(0)
                .name("Introduction to Cognitive Science")
                .build();
        this.course1 = factory.createProjection(CourseDetails.class, course1);
        this.course1Min = factory.createProjection(CourseSummary.class, course1);
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

        newCourseRequest = new NewCourseRequest();
        newCourseRequest.setEcts(10);
        newCourseRequest.setName("Logic II");
        newCourseRequest.setLanguage("pl");

        addedCourse = Course.builder()
                .id(17)
                .name("Logic II")
                .ects(10)
                .language("pl")
                .build();

        updateCourseRequest = new UpdateCourseRequest();
        updateCourseRequest.setEcts(10);
        updateCourseRequest.setName("Pragmatics");

        updatedCourse = Course.builder()
                .id(1)
                .name("Pragmatics")
                .ects(10)
                .build();

        yearRequest = new CourseYearRequest();

        Course course5 = Course.builder()
                .id(5)
                .name("Game Theory")
                .build();
        addedCourseYear = CourseYear.builder()
                .id(17)
                .parent(course5)
                .build();

        CourseYear year0 = CourseYear.builder()
                .id(0)
                .build();
        CourseYear year1 = CourseYear.builder()
                .id(1)
                .build();

        CourseYearForPage year0ForPage = factory.createProjection(CourseYearForPage.class, year0);
        CourseYearForPage year1ForPage = factory.createProjection(CourseYearForPage.class, year1);

        List<CourseYearForPage> courseYearList = new ArrayList<>();
        courseYearList.add(year0ForPage);
        courseYearList.add(year1ForPage);
        courseYearsPage = new PageImpl<>(courseYearList);
    }

    private void injectMocks() {
        CourseController courseController = new CourseController(courseService, courseYearService);
        RestAssuredMockMvc.standaloneSetup(courseController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToAllRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses")
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceListOfCourses() {
        BDDMockito.given(courseService.getAllCourses(anyInt(), anyInt()))
                .willReturn(coursePage);
        injectMocks();
        given()
                .when()
                .get("/courses")
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
    void shouldTakePageAndSizeFromGetCoursesRequest() {
        injectMocks();
        given()
                .queryParam("page", 7)
                .queryParam("size", 5)
                .when()
                .get("/courses")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getAllCourses(7, 5);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForCoursesRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getAllCourses(0, 20);
    }

    @Test
    void shouldRespondToGetCourseRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}", 0)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceCourse() {
        BDDMockito.given(courseService.getCourse(0))
                .willReturn(course1);
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}", 0)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(0))
                .body("name", equalTo("Introduction to Cognitive Science"));
    }

    @Test
    void shouldAskServiceForCourse() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}", 0)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getCourse(0);
    }

    @Test
    void shouldRespondToAddNewCourseRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(newCourseRequest)
                .when()
                .post("/courses")
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldCreateNewCourse() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(newCourseRequest)
                .when()
                .post("/courses")
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<NewCourseRequest> requestCaptor = ArgumentCaptor.forClass(NewCourseRequest.class);
        BDDMockito.then(courseService)
                .should(times(1))
                .addNewCourse(requestCaptor.capture());

        NewCourseRequest capturedRequest = requestCaptor.getValue();
        assertEquals("Logic II", capturedRequest.getName());
        assertEquals("pl", capturedRequest.getLanguage());
        assertEquals(10, capturedRequest.getEcts());
    }

    @Test
    void shouldReturnAddedCourse() {
        BDDMockito.given(courseService.addNewCourse(any(NewCourseRequest.class)))
                .willReturn(addedCourse);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(newCourseRequest)
                .when()
                .post("/courses")
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(17))
                .body("name", equalTo("Logic II"))
                .body("language", equalTo("pl"))
                .body("ects", equalTo(10));
    }

    @Test
    void shouldRespondToUpdateCourseRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateCourseRequest)
                .when()
                .put("/courses/{courseId}", 1)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateCourse() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateCourseRequest)
                .when()
                .put("/courses/{courseId}", 1)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<UpdateCourseRequest> requestCaptor = ArgumentCaptor.forClass(UpdateCourseRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(courseService)
                .should(times(1))
                .updateCourse(requestCaptor.capture(), idCaptor.capture());

        Integer id = idCaptor.getValue();
        assertEquals(1, id);

        UpdateCourseRequest capturedRequest = requestCaptor.getValue();
        assertEquals("Pragmatics", capturedRequest.getName());
        assertEquals(10, capturedRequest.getEcts());
    }

    @Test
    void shouldReturnUpdatedCourse() {
        BDDMockito.given(courseService.updateCourse(any(UpdateCourseRequest.class), anyInt()))
                .willReturn(updatedCourse);
        injectMocks();

        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateCourseRequest)
                .when()
                .put("/courses/{courseId}", 1)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("Pragmatics"))
                .body("ects", equalTo(10));
    }

    @Test
    void shouldRespondToDeleteRequest() {
        injectMocks();
        given()
                .when()
                .delete("/courses/{courseId}", 9)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteCourse() {
        injectMocks();
        given()
                .when()
                .delete("/courses/{courseId}", 9)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .deleteCourse(9);
    }

    @Test
    void shouldRespondToAddCourseYearRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(yearRequest)
                .when()
                .post("/courses/{courseId}/years", 9)
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldAddCourseYear() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(yearRequest)
                .when()
                .post("/courses/{courseId}/years", 5)
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<CourseYearRequest> requestCaptor = ArgumentCaptor.forClass(CourseYearRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        BDDMockito.then(courseYearService)
                .should(times(1))
                .addNewCourseYear(idCaptor.capture(), requestCaptor.capture());

        CourseYearRequest capturedRequest = requestCaptor.getValue();
        Integer parentId = idCaptor.getValue();
        assertEquals(5, parentId);
    }

    @Test
    void shouldReturnCreatedCourseYear() {
        BDDMockito.given(courseYearService.addNewCourseYear(anyInt(), any(CourseYearRequest.class)))
                .willReturn(addedCourseYear);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(yearRequest)
                .when()
                .post("/courses/{courseId}/years", 5)
                .then()
                .statusCode(CREATED.value())
                //.body("parent.id", equalTo(5))
                //.body("parent.name", equalTo("Game Theory"))
                .body("id", equalTo(17));
    }

    @Test
    void shouldRespondToGetAllCourseYearsRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/years", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetAllCourseYearsRequest() {
        injectMocks();
        given()
                .queryParam("page", 7)
                .queryParam("size", 5)
                .when()
                .get("/courses/{courseId}/years", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseYearService)
                .should(times(1))
                .getYearsForCourse(5, 7, 5);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetAllCourseYearsRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/years", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseYearService)
                .should(times(1))
                .getYearsForCourse(5,0, 20);
    }

    @Test
    void shouldProduceListOfCourseYears() {
        BDDMockito.given(courseYearService.getYearsForCourse(anyInt(), anyInt(), anyInt()))
                .willReturn(courseYearsPage);
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/years", 5)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(2));
    }

    @Test
    void shouldRespondToGetAllActiveCourseYearsRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/years/active", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetAllActiveCourseYearsRequest() {
        injectMocks();
        given()
                .queryParam("page", 7)
                .queryParam("size", 5)
                .when()
                .get("/courses/{courseId}/years/active", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseYearService)
                .should(times(1))
                .getActiveYearsForCourse(5, 7, 5);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetAllActiveCourseYearsRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/years/active", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseYearService)
                .should(times(1))
                .getActiveYearsForCourse(5,0, 20);
    }

    @Test
    void shouldProduceListOfActiveCourseYears() {
        BDDMockito.given(courseYearService.getActiveYearsForCourse(anyInt(), anyInt(), anyInt()))
                .willReturn(courseYearsPage);
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/years/active", 5)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(2));
    }

    @Test
    void shouldRespondToGetCourseMinRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/min", 0)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceCourseMin() {
        BDDMockito.given(courseService.getCourseMin(0))
                .willReturn(course1Min);
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/min", 0)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(0))
                .body("name", equalTo("Introduction to Cognitive Science"));
    }

    @Test
    void shouldAskServiceForCourseMin() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/min", 0)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseService)
                .should(times(1))
                .getCourseMin(0);
    }
}