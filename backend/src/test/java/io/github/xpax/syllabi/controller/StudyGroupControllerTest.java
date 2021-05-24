package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseYear;
import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.StudyGroupDetails;
import io.github.xpax.syllabi.entity.dto.StudyGroupRequest;
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
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class StudyGroupControllerTest {
    @Mock
    private StudyGroupService studyGroupService;

    private StudyGroup group;
    private StudyGroupDetails groupDetails;
    private StudyGroupRequest studyGroupRequest;
    private StudyGroup updatedStudyGroup;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        group = StudyGroup.builder()
                .id(5)
                .studentLimit(20)
                .build();

        this.groupDetails = factory.createProjection(StudyGroupDetails.class, group);

        studyGroupRequest = new StudyGroupRequest();
        studyGroupRequest.setOngoing(true);

        CourseYear yearWithId1 = CourseYear.builder()
                .id(1)
                .build();
        updatedStudyGroup = StudyGroup.builder()
                .id(7)
                .year(yearWithId1)
                .ongoing(true)
                .build();
    }

    private void injectMocks() {
        StudyGroupController studyGroupController = new StudyGroupController(studyGroupService);
        RestAssuredMockMvc.standaloneSetup(studyGroupController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToGetStudyGroupRequest() {
        injectMocks();
        given()
                .when()
                .get("/groups/{groupId}", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceStudyGroup() {
        BDDMockito.given(studyGroupService.getStudyGroup(5))
                .willReturn(groupDetails);
        injectMocks();
        given()
                .when()
                .get("/groups/{groupId}", 5)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(5))
                .body("studentLimit", equalTo(20));
    }

    @Test
    void shouldRespondToDeleteRequest() {
        injectMocks();
        given()
                .when()
                .delete("/groups/{groupId}", 5)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteGroup() {
        injectMocks();
        given()
                .when()
                .delete("/groups/{groupId}", 5)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(studyGroupService)
                .should(times(1))
                .deleteStudyGroup(5);
    }

    @Test
    void shouldRespondToUpdateGroupRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(studyGroupRequest)
                .when()
                .put("/groups/{groupId}", 7)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateGroup() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(studyGroupRequest)
                .when()
                .put("/groups/{groupId}", 7)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<StudyGroupRequest> requestCaptor = ArgumentCaptor.forClass(StudyGroupRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(studyGroupService)
                .should(times(1))
                .updateStudyGroup(requestCaptor.capture(), idCaptor.capture());
        StudyGroupRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(7, id);
        assertEquals(true, request.getOngoing());
    }

    @Test
    void shouldProduceUpdatedGroup() {
        BDDMockito.given(studyGroupService.updateStudyGroup(any(StudyGroupRequest.class), anyInt()))
                .willReturn(updatedStudyGroup);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(studyGroupRequest)
                .when()
                .put("/groups/{groupId}", 7)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(7))
                .body("ongoing", equalTo(true));
        //.body("year.id", equalTo(1));
    }
}
