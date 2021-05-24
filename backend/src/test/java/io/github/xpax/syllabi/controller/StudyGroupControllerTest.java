package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.StudyGroup;
import io.github.xpax.syllabi.entity.dto.StudyGroupDetails;
import io.github.xpax.syllabi.service.StudyGroupService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.MockMvcConfig;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class StudyGroupControllerTest {
    @Mock
    private StudyGroupService studyGroupService;

    private StudyGroup group;
    private StudyGroupDetails groupDetails;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        group = StudyGroup.builder()
                .id(5)
                .studentLimit(20)
                .build();

        this.groupDetails = factory.createProjection(StudyGroupDetails.class, group);
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
}
