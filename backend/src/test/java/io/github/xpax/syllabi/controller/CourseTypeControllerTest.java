package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.CourseType;
import io.github.xpax.syllabi.entity.dto.CourseTypeRequest;
import io.github.xpax.syllabi.service.CourseTypeService;
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
class CourseTypeControllerTest {
    @Mock
    private CourseTypeService courseTypeService;

    private CourseTypeRequest typeRequest;
    private CourseType lecture;
    private CourseTypeRequest updateTypeRequest;
    private CourseType updatedLecture;
    private Page<CourseType> typePage;

    @BeforeEach
    void setUp() {
        typeRequest = new CourseTypeRequest();
        typeRequest.setName("Lecture");

        lecture = CourseType.builder()
                .id(3)
                .name("Lecture")
                .build();

        updateTypeRequest = new CourseTypeRequest();
        updateTypeRequest.setName("lecture");

        updatedLecture = CourseType.builder()
                .id(3)
                .name("lecture")
                .build();

        CourseType lab = CourseType.builder()
                .id(4)
                .name("Laboratory")
                .build();
        List<CourseType> types = new ArrayList<>();
        types.add(lecture);
        types.add(lab);
        typePage = new PageImpl<>(types);
    }

    private void injectMocks() {
        CourseTypeController courseTypeController = new CourseTypeController(courseTypeService);
        RestAssuredMockMvc.standaloneSetup(courseTypeController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToCreateCourseTypeRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(typeRequest)
                .when()
                .post("/types")
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldCreateCourseType() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(typeRequest)
                .when()
                .post("/types")
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<CourseTypeRequest> requestCaptor = ArgumentCaptor.forClass(CourseTypeRequest.class);
        BDDMockito.then(courseTypeService)
                .should(times(1))
                .addNewCourseType(requestCaptor.capture());
        CourseTypeRequest request = requestCaptor.getValue();

        assertEquals("Lecture", request.getName());
    }

    @Test
    void shouldReturnCreatedCourseType() {
        BDDMockito.given(courseTypeService.addNewCourseType(any(CourseTypeRequest.class)))
                .willReturn(lecture);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(typeRequest)
                .when()
                .post("/types")
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(3))
                .body("name", equalTo("Lecture"));
    }

    @Test
    void shouldRespondToGetCourseTypeRequest() {
        injectMocks();
        given()
                .when()
                .get("/types/{typeId}", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceCourseType() {
        BDDMockito.given(courseTypeService.getCourseType(3))
                .willReturn(lecture);
        injectMocks();
        given()
                .when()
                .get("/types/{typeId}", 3)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(3))
                .body("name", equalTo("Lecture"));
    }

    @Test
    void shouldRespondToDeleteCourseTypeRequest() {
        injectMocks();
        given()
                .when()
                .delete("/types/{typeId}", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteCourseType() {
        injectMocks();
        given()
                .when()
                .delete("/types/{typeId}", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseTypeService)
                .should(times(1))
                .deleteCourseType(3);
    }

    @Test
    void shouldRespondToUpdateCourseTypeRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateTypeRequest)
                .when()
                .put("/types/{typeId}", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldUpdateCourseType() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateTypeRequest)
                .when()
                .put("/types/{typeId}", 3)
                .then()
                .statusCode(OK.value());

        ArgumentCaptor<CourseTypeRequest> requestCaptor = ArgumentCaptor.forClass(CourseTypeRequest.class);
        ArgumentCaptor<Integer> idCaptor = ArgumentCaptor.forClass(Integer.class);
        BDDMockito.then(courseTypeService)
                .should(times(1))
                .updateCourseType(requestCaptor.capture(), idCaptor.capture());
        CourseTypeRequest request = requestCaptor.getValue();
        Integer id = idCaptor.getValue();

        assertEquals("lecture", request.getName());
        assertEquals(3, id);
    }

    @Test
    void shouldProduceUpdatedCourseType() {
        BDDMockito.given(courseTypeService.updateCourseType(any(CourseTypeRequest.class), anyInt()))
                .willReturn(updatedLecture);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(updateTypeRequest)
                .when()
                .put("/types/{typeId}", 3)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(3))
                .body("name", equalTo("lecture"));
    }

    @Test
    void shouldRespondToGetAllCourseTypesRequest() {
        injectMocks();
        given()
                .when()
                .get("/types")
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetAllCourseTypesRequest() {
        injectMocks();
        given()
                .queryParam("page", 1)
                .queryParam("size", 15)
                .when()
                .get("/types")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseTypeService)
                .should(times(1))
                .getAllCourseTypes(1, 15);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetAllCourseTypesRequest() {
        injectMocks();
        given()
                .when()
                .get("/types")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(courseTypeService)
                .should(times(1))
                .getAllCourseTypes(0, 20);
    }

    @Test
    void shouldProducePageOfAllCourseTypes() {
        BDDMockito.given(courseTypeService.getAllCourseTypes(anyInt(), anyInt()))
                .willReturn(typePage);
        injectMocks();
        given()
                .when()
                .get("/types")
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(2))
                .body("content[0].id", equalTo(3))
                .body("content[0].name", equalTo("Lecture"))
                .body("content[1].id", equalTo(4))
                .body("content[1].name", equalTo("Laboratory"))
                .body("numberOfElements", equalTo(2));
    }
}
