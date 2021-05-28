package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Semester;
import io.github.xpax.syllabi.service.SemesterService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.config.MockMvcConfig;
import io.restassured.module.mockmvc.config.RestAssuredMockMvcConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class SemesterControllerTest {
    @Mock
    private SemesterService semesterService;

    private Semester semester;

    @BeforeEach
    void setUp() {
        semester = Semester.builder()
                .name("Semester")
                .id(1)
                .number(1)
                .build();
    }

    private void injectMocks() {
        SemesterController controller = new SemesterController(semesterService);
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
}
