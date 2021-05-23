package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseLiterature;
import io.github.xpax.syllabi.entity.dto.LiteratureForPage;
import io.github.xpax.syllabi.service.CourseLiteratureService;
import io.github.xpax.syllabi.service.GroupLiteratureService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.MockMvcConfig;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
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
}
