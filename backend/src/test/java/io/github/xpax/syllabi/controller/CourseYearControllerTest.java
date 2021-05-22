package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.CourseYearDetails;
import io.github.xpax.syllabi.entity.dto.CourseYearRequest;
import io.github.xpax.syllabi.entity.dto.StudyGroupForPage;
import io.github.xpax.syllabi.entity.dto.StudyGroupRequest;
import io.github.xpax.syllabi.service.CourseYearService;
import io.github.xpax.syllabi.service.StudyGroupService;
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
public class CourseYearControllerTest {
    @Mock
    private CourseYearService courseYearService;
    @Mock
    private StudyGroupService studyGroupService;
    private CourseYearDetails yearWithId1;
    private CourseYear yearWithId1Updated;
    private CourseYearRequest courseYearRequest;
    private StudyGroupRequest studyGroupRequest;
    private StudyGroup createdStudyGroup;
    private Page<StudyGroupForPage> groupPage;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        CourseYear yearWithId1 = CourseYear.builder()
                .id(1)
                .build();
        this.yearWithId1 = factory.createProjection(CourseYearDetails.class, yearWithId1);
        yearWithId1Updated = CourseYear.builder()
                .id(1)
                .commentary("comment")
                .build();
        courseYearRequest = new CourseYearRequest();
        courseYearRequest.setCommentary("comment");
        studyGroupRequest = new StudyGroupRequest();
        studyGroupRequest.setOngoing(true);

        createdStudyGroup = StudyGroup.builder()
                .id(7)
                .year(yearWithId1)
                .ongoing(true)
                .build();

        StudyGroupForPage createdStudyGroupProj = factory.createProjection(StudyGroupForPage.class, createdStudyGroup);

        List<StudyGroupForPage> groupList = new ArrayList<>();
        groupList.add(createdStudyGroupProj);
        groupPage = new PageImpl<>(groupList);
    }

    private void injectMocks() {
        CourseYearController courseYearController = new CourseYearController(courseYearService, studyGroupService);
        RestAssuredMockMvc.standaloneSetup(courseYearController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToGetCourseYearRequest() {
        injectMocks();
        given()
                .when()
                .get("/years/{yearId}", 4)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceCourseYear() {
        BDDMockito.given(courseYearService.getCourseYear(1))
                .willReturn(yearWithId1);
        injectMocks();
        given()
                .when()
                .get("/years/{yearId}", 1)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1));
    }

    @Test
    void shouldRespondToDeleteCourseYearRequest() {
        injectMocks();
        given()
                .when()
                .delete("/years/{yearId}", 4)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteCourseYear() {
        injectMocks();
        given()
                .when()
                .delete("/years/{yearId}", 4)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseYearService)
                .should(times(1))
                .deleteCourseYear(4);
    }

    @Test
    void shouldRespondTuUpdateCourseYearRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(courseYearRequest)
                .when()
                .put("/years/{yearId}", 1)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateCourseYear() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(courseYearRequest)
                .when()
                .put("/years/{yearId}", 1)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<CourseYearRequest> requestCaptor = ArgumentCaptor.forClass(CourseYearRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(courseYearService)
                .should(times(1))
                .updateCourseYear(requestCaptor.capture(), idCaptor.capture());
        CourseYearRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(1, id);
        assertEquals("comment", request.getCommentary());
    }

    @Test
    void shouldProduceUpdatedCourse() {
        BDDMockito.given(courseYearService.updateCourseYear(any(CourseYearRequest.class), anyInt()))
                .willReturn(yearWithId1Updated);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(courseYearRequest)
                .when()
                .put("/years/{yearId}", 1)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(1))
                .body("commentary", equalTo("comment"));
    }

    @Test
    void shouldRespondToAddStudyGroupRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(studyGroupRequest)
                .when()
                .post("/years/{yearId}/groups", 1)
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldAddStudyGroup() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(studyGroupRequest)
                .when()
                .post("/years/{yearId}/groups", 1)
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<StudyGroupRequest> requestCaptor = ArgumentCaptor.forClass(StudyGroupRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(studyGroupService)
                .should(times(1))
                .addNewStudyGroup(requestCaptor.capture(), idCaptor.capture());
        StudyGroupRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(1, id);
        assertEquals(true, request.getOngoing());
    }

    @Test
    void shouldProduceCreatedStudyGroup() {
        BDDMockito.given(studyGroupService.addNewStudyGroup(any(StudyGroupRequest.class), anyInt()))
                .willReturn(createdStudyGroup);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(studyGroupRequest)
                .when()
                .post("/years/{yearId}/groups", 1)
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(7))
                .body("ongoing", equalTo(true));
    }

    @Test
    void shouldRespondToGetStudyGroupsRequest() {
        injectMocks();
        given()
                .when()
                .get("/years/{yearId}/groups", 1)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromStudyGroupsRequest() {
        injectMocks();
        given()
                .queryParam("page", 2)
                .queryParam("size", 15)
                .when()
                .get("/years/{yearId}/groups", 1)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(studyGroupService)
                .should(times(1))
                .getAllGroupsByCourseYear(1, 2, 15);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForStudyGroupsRequest() {
        injectMocks();
        given()
                .when()
                .get("/years/{yearId}/groups", 1)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(studyGroupService)
                .should(times(1))
                .getAllGroupsByCourseYear(1, 0, 20);
    }

    @Test
    void shouldProducePageOfStudyGroups() {
        BDDMockito.given(studyGroupService.getAllGroupsByCourseYear(anyInt(), anyInt(), anyInt()))
                .willReturn(groupPage);
        injectMocks();
        given()
                .when()
                .get("/years/{yearId}/groups", 1)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(1))
                .body("content[0].id", equalTo(7))
                .body("content[0].ongoing", equalTo(true))
                .body("numberOfElements", equalTo(1));
    }
}
