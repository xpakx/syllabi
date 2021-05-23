package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.dto.InstituteDetails;
import io.github.xpax.syllabi.entity.dto.InstituteForPage;
import io.github.xpax.syllabi.entity.dto.InstituteRequest;
import io.github.xpax.syllabi.service.InstituteService;
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
class InstituteControllerTest {
    @Mock
    private InstituteService instituteService;

    private Page<InstituteForPage> institutePage;
    private Institute institute1;
    private InstituteDetails institute1Det;
    private Institute createdInstitute;
    private InstituteRequest instituteRequest;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @BeforeEach
    void setUp() {
        institute1 = Institute.builder()
                .id(7)
                .name("Department of Philosophy")
                .build();
        Institute institute2 = Institute.builder()
                .id(15)
                .name("Institute of Computer Science")
                .build();
        Institute institute3 = Institute.builder()
                .id(21)
                .name("Department of Physics")
                .build();

        institute1Det = factory.createProjection(InstituteDetails.class, institute1);

        List<InstituteForPage> instituteList = new ArrayList<>();
        instituteList.add(factory.createProjection(InstituteForPage.class, institute1));
        instituteList.add(factory.createProjection(InstituteForPage.class, institute2));
        instituteList.add(factory.createProjection(InstituteForPage.class, institute3));
        institutePage = new PageImpl<>(instituteList);

        instituteRequest = new InstituteRequest();
        instituteRequest.setName("Institute of Experimental Philosophy");
        instituteRequest.setParentId(7);

        createdInstitute = Institute.builder()
                .id(13)
                .name("Institute of Experimental Philosophy")
                .parent(institute1)
                .build();
    }

    private void injectMocks() {
        InstituteController instituteController = new InstituteController(instituteService);
        RestAssuredMockMvc.standaloneSetup(instituteController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToGetAllInstitutesRequest() {
        injectMocks();
        given()
                .when()
                .get("/institutes")
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldTakePageAndSizeFromGetAllInstitutesRequest() {
        injectMocks();
        given()
                .queryParam("page", 7)
                .queryParam("size", 5)
                .when()
                .get("/institutes")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(instituteService)
                .should(times(1))
                .getAllInstitutes(7, 5);
    }

    @Test
    void shouldUseDefaultPageAndSizeValuesForGetAllInstitutesRequest() {
        injectMocks();
        given()
                .when()
                .get("/institutes")
                .then()
                .statusCode(OK.value());

        BDDMockito.then(instituteService)
                .should(times(1))
                .getAllInstitutes(0, 20);
    }

    @Test
    void shouldProducePageOfInstitutes() {
        BDDMockito.given(instituteService.getAllInstitutes(anyInt(), anyInt()))
                .willReturn(institutePage);
        injectMocks();
        given()
                .when()
                .get("/institutes")
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(3))
                .body("content[0].id", equalTo(7))
                .body("content[0].name", equalTo("Department of Philosophy"))
                .body("content[1].id", equalTo(15))
                .body("content[1].name", equalTo("Institute of Computer Science"))
                .body("content[2].id", equalTo(21))
                .body("content[2].name", equalTo("Department of Physics"))
                .body("numberOfElements", equalTo(3));
    }

    @Test
    void shouldRespondToDeleteInstituteRequest() {
        injectMocks();
        given()
                .when()
                .delete("/institutes/{instituteId}", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldDeleteInstitute() {
        injectMocks();
        given()
                .when()
                .delete("/institutes/{instituteId}", 3)
                .then()
                .statusCode(OK.value());

        BDDMockito.then(instituteService)
                .should(times(1))
                .deleteInstitute(3);
    }

    @Test
    void shouldRespondToGetInstituteRequest() {
        injectMocks();
        given()
                .when()
                .get("/institutes/{instituteId}", 3)
                .then()
                .statusCode(OK.value());
    }

    @Test
    void shouldProduceInstitute() {
        BDDMockito.given(instituteService.getInstitute(7))
                .willReturn(institute1Det);
        injectMocks();
        given()
                .when()
                .get("/institutes/{instituteId}", 7)
                .then()
                .statusCode(OK.value())
                .body("id", equalTo(7))
                .body("name", equalTo("Department of Philosophy"));
    }

    @Test
    void shouldRespondToAddInstituteRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(instituteRequest)
                .when()
                .post("/institutes")
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldAddInstitute() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(instituteRequest)
                .when()
                .post("/institutes")
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<InstituteRequest> requestCaptor = ArgumentCaptor.forClass(InstituteRequest.class);

        BDDMockito.then(instituteService)
                .should(times(1))
                .addNewInstitute(requestCaptor.capture());
        InstituteRequest request = requestCaptor.getValue();

        assertEquals("Institute of Experimental Philosophy", request.getName());
        assertEquals(7, request.getParentId());
    }

    @Test
    void shouldProduceCreatedInstitute() {
        BDDMockito.given(instituteService.addNewInstitute(any(InstituteRequest.class)))
                .willReturn(createdInstitute);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(instituteRequest)
                .when()
                .post("/institutes")
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(13))
                .body("name", equalTo("Institute of Experimental Philosophy"));
    }
}
