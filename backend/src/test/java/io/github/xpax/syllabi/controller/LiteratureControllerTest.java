package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.GroupLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.entity.dto.LiteratureRequest;
import io.github.xpax.syllabi.service.CourseLiteratureService;
import io.github.xpax.syllabi.service.GroupLiteratureService;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class LiteratureControllerTest {
    @Mock
    private CourseLiteratureService courseLiteratureService;
    @Mock
    private GroupLiteratureService groupLiteratureService;

    private Page<LiteratureForPage> courseLiteraturePage;
    private CourseLiterature courseLiterature2;
    private LiteratureRequest literatureRequest;
    private CourseLiterature createdCourseLiterature;
    private Page<LiteratureForPage> groupLiteraturePage;
    private GroupLiterature groupLiterature2;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        CourseLiterature courseLiterature1 = CourseLiterature.builder()
                .id(1)
                .author("Dorothy Cheney, Robert Seyfarth")
                .title("Baboon Metaphysics: The Evolution of a Social Mind")
                .build();
        courseLiterature2 = CourseLiterature.builder()
                .id(2)
                .author("Barbara Smuts")
                .title("Primate Societies")
                .build();

        LiteratureForPage courseLiterature1Proj = factory.createProjection(
                LiteratureForPage.class,
                courseLiterature1
        );
        LiteratureForPage courseLiterature2Proj = factory.createProjection(
                LiteratureForPage.class,
                courseLiterature2
        );
        List<LiteratureForPage> courseLiteratureList = new ArrayList<>();
        courseLiteratureList.add(courseLiterature1Proj);
        courseLiteratureList.add(courseLiterature2Proj);
        courseLiteraturePage = new PageImpl<>(courseLiteratureList);

        literatureRequest = new LiteratureRequest();
        literatureRequest.setAuthor("Shirley C. Strum");
        literatureRequest.setObligatory(false);
        literatureRequest.setTitle("Almost Human: A Journey into the World of Baboons");

        createdCourseLiterature = CourseLiterature.builder()
                .id(0)
                .author("Shirley C. Strum")
                .obligatory(false)
                .title("Almost Human: A Journey into the World of Baboons")
                .build();

        GroupLiterature groupLiterature1 = GroupLiterature.builder()
                .id(1)
                .author("Dorothy Cheney, Robert Seyfarth")
                .title("Baboon Metaphysics: The Evolution of a Social Mind")
                .build();
        groupLiterature2 = GroupLiterature.builder()
                .id(2)
                .author("Barbara Smuts")
                .title("Primate Societies")
                .build();

        LiteratureForPage groupLiterature1Proj = factory.createProjection(
                LiteratureForPage.class,
                groupLiterature1
        );
        LiteratureForPage groupLiterature2Proj = factory.createProjection(
                LiteratureForPage.class,
                groupLiterature2
        );
        List<LiteratureForPage> groupLiteratureList = new ArrayList<>();
        groupLiteratureList.add(groupLiterature1Proj);
        groupLiteratureList.add(groupLiterature2Proj);
        groupLiteraturePage = new PageImpl<>(groupLiteratureList);
    }

    private void injectMocks() {
        LiteratureController literatureController = new LiteratureController(courseLiteratureService, groupLiteratureService);
        RestAssuredMockMvc.standaloneSetup(literatureController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToGetAllCourseLiteratureRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/literature", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetAllCourseLiteratureRequest() {
        injectMocks();
        given()
                .queryParam("page", 2)
                .queryParam("size", 15)
                .when()
                .get("/courses/{courseId}/literature", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseLiteratureService)
                .should(times(1))
                .getAllLiterature(2, 15, 3);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetAllCourseLiteratureRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/literature", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseLiteratureService)
                .should(times(1))
                .getAllLiterature(0, 20, 3);
    }

    @Test
    void shouldProducePageOfCourseLiterature() {
        BDDMockito.given(courseLiteratureService.getAllLiterature(anyInt(), anyInt(), anyInt()))
                .willReturn(courseLiteraturePage);
        injectMocks();
        given()
                .when()
                .get("/courses/{courseId}/literature", 3)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(2))
                .body("content[0].id", equalTo(1))
                .body("content[0].author", equalTo("Dorothy Cheney, Robert Seyfarth"))
                .body("content[0].title", equalTo("Baboon Metaphysics: The Evolution of a Social Mind"))
                .body("content[1].id", equalTo(2))
                .body("content[1].author", equalTo("Barbara Smuts"))
                .body("content[1].title", equalTo("Primate Societies"))
                .body("numberOfElements", equalTo(2));
    }

    @Test
    void shouldRespondToDeleteCourseLiteratureRequest() {
        injectMocks();
        given()
                .when()
                .delete("/courses/literature/{literatureId}", 4)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteCourseLiterature() {
        injectMocks();
        given()
                .when()
                .delete("/courses/literature/{literatureId}", 4)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseLiteratureService)
                .should(times(1))
                .deleteLiterature(4);
    }

    @Test
    void shouldRespondToGetCourseLiteratureRequest() {
        injectMocks();
        given()
                .when()
                .get("/courses/literature/{literatureId}", 2)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceCourseLiterature() {
        BDDMockito.given(courseLiteratureService.getLiterature(2))
                .willReturn(courseLiterature2);
        injectMocks();
        given()
                .when()
                .get("/courses/literature/{literatureId}", 2)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(2))
                .body("author", equalTo("Barbara Smuts"))
                .body("title", equalTo("Primate Societies"));
    }

    @Test
    void shouldRespondToAddCourseLiteratureRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(literatureRequest)
                .when()
                .post("/courses/{courseId}/literature", 1)
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldAddCourseLiterature() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(literatureRequest)
                .when()
                .post("/courses/{courseId}/literature", 1)
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<LiteratureRequest> requestCaptor = ArgumentCaptor.forClass(LiteratureRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(courseLiteratureService)
                .should(times(1))
                .addNewLiterature(requestCaptor.capture(), idCaptor.capture());
        LiteratureRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(1, id);
        assertEquals("Shirley C. Strum", request.getAuthor());
        assertEquals("Almost Human: A Journey into the World of Baboons", request.getTitle());
        assertFalse(request.getObligatory());
    }

    @Test
    void shouldProduceCreatedCourseLiterature() {
        BDDMockito.given(courseLiteratureService.addNewLiterature(any(LiteratureRequest.class), anyInt()))
                .willReturn(createdCourseLiterature);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(literatureRequest)
                .when()
                .post("/courses/{courseId}/literature", 1)
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(0))
                .body("obligatory", equalTo(false))
                .body("title", equalTo("Almost Human: A Journey into the World of Baboons"))
                .body("author", equalTo("Shirley C. Strum"));
    }

    @Test
    void shouldRespondToUpdateCourseLiteratureRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(literatureRequest)
                .when()
                .put("/courses/literature/{literatureId}", 0)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateCourseLiterature() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(literatureRequest)
                .when()
                .put("/courses/literature/{literatureId}", 0)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<LiteratureRequest> requestCaptor = ArgumentCaptor.forClass(LiteratureRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);

        BDDMockito.then(courseLiteratureService)
                .should(times(1))
                .updateLiterature(requestCaptor.capture(), idCaptor.capture());
        LiteratureRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals(0, id);
        assertEquals("Shirley C. Strum", request.getAuthor());
        assertEquals("Almost Human: A Journey into the World of Baboons", request.getTitle());
        assertFalse(request.getObligatory());
    }

    @Test
    void shouldProduceUpdatedCourseLiterature() {
        BDDMockito.given(courseLiteratureService.updateLiterature(any(LiteratureRequest.class), anyInt()))
                .willReturn(createdCourseLiterature);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(literatureRequest)
                .when()
                .put("/courses/literature/{literatureId}", 0)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(0))
                .body("obligatory", equalTo(false))
                .body("title", equalTo("Almost Human: A Journey into the World of Baboons"))
                .body("author", equalTo("Shirley C. Strum"));
    }

    @Test
    void shouldRespondToGetAllGroupLiteratureRequest() {
        injectMocks();
        given()
                .when()
                .get("groups/{groupId}/literature", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetAllGroupLiteratureRequest() {
        injectMocks();
        given()
                .queryParam("page", 2)
                .queryParam("size", 15)
                .when()
                .get("groups/{groupId}/literature", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(groupLiteratureService)
                .should(times(1))
                .getAllLiterature(2, 15, 3);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetAllGroupLiteratureRequest() {
        injectMocks();
        given()
                .when()
                .get("groups/{groupId}/literature", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(groupLiteratureService)
                .should(times(1))
                .getAllLiterature(0, 20, 3);
    }

    @Test
    void shouldProducePageOfGroupLiterature() {
        BDDMockito.given(groupLiteratureService.getAllLiterature(anyInt(), anyInt(), anyInt()))
                .willReturn(groupLiteraturePage);
        injectMocks();
        given()
                .when()
                .get("groups/{groupId}/literature", 3)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(2))
                .body("content[0].id", equalTo(1))
                .body("content[0].author", equalTo("Dorothy Cheney, Robert Seyfarth"))
                .body("content[0].title", equalTo("Baboon Metaphysics: The Evolution of a Social Mind"))
                .body("content[1].id", equalTo(2))
                .body("content[1].author", equalTo("Barbara Smuts"))
                .body("content[1].title", equalTo("Primate Societies"))
                .body("numberOfElements", equalTo(2));
    }

    @Test
    void shouldRespondToDeleteGroupLiteratureRequest() {
        injectMocks();
        given()
                .when()
                .delete("/groups/literature/{literatureId}", 4)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteGroupLiterature() {
        injectMocks();
        given()
                .when()
                .delete("/groups/literature/{literatureId}", 4)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(groupLiteratureService)
                .should(times(1))
                .deleteLiterature(4);
    }
}
