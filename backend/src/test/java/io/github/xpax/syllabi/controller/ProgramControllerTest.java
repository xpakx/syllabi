package io.github.xpax.syllabi.controller;

import io.github.xpax.syllabi.entity.Program;
import io.github.xpax.syllabi.entity.dto.ProgramRequest;
import io.github.xpax.syllabi.service.CourseService;
import io.github.xpax.syllabi.service.ProgramService;
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
import static org.mockito.Mockito.times;
import static org.springframework.http.HttpStatus.CREATED;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class ProgramControllerTest {
    @Mock
    private CourseService courseService;
    @Mock
    private ProgramService programService;

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();
    private ProgramRequest programRequest;
    private Program addedProgram;

    @BeforeEach
    void setUp() {
        programRequest = new ProgramRequest();
        programRequest.setName("Philosophy");
        programRequest.setDescription("Philosophy program");
        addedProgram = Program.builder()
                .id(17)
                .name("Philosophy")
                .description("Philosophy program")
                .build();
    }

    private void injectMocks() {
        ProgramController programController = new ProgramController(courseService, programService);
        RestAssuredMockMvc.standaloneSetup(programController);
        RestAssuredMockMvc.config = new RestAssuredMockMvcConfig()
                .mockMvcConfig(MockMvcConfig.mockMvcConfig().dontAutomaticallyApplySpringSecurityMockMvcConfigurer());
    }

    @Test
    void shouldRespondToAddNewProgramRequest() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .post("/programs")
                .then()
                .statusCode(CREATED.value());
    }

    @Test
    void shouldCreateNewProgram() {
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .post("/programs")
                .then()
                .statusCode(CREATED.value());

        ArgumentCaptor<ProgramRequest> requestCaptor = ArgumentCaptor.forClass(ProgramRequest.class);
        BDDMockito.then(programService)
                .should(times(1))
                .addNewProgram(requestCaptor.capture());

        ProgramRequest capturedRequest = requestCaptor.getValue();
        assertEquals("Philosophy", capturedRequest.getName());
        assertEquals("Philosophy program", capturedRequest.getDescription());
    }

    @Test
    void shouldReturnAddedProgram() {
        BDDMockito.given(programService.addNewProgram(any(ProgramRequest.class)))
                .willReturn(addedProgram);
        injectMocks();
        given()
                .contentType(ContentType.JSON)
                .body(programRequest)
                .when()
                .post("/programs")
                .then()
                .statusCode(CREATED.value())
                .body("id", equalTo(17))
                .body("name", equalTo("Philosophy"))
                .body("description", equalTo("Philosophy program"));
    }
}
