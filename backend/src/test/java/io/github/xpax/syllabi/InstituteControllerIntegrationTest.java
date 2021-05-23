package io.github.xpax.syllabi;

import io.github.xpax.syllabi.entity.Institute;
import io.github.xpax.syllabi.entity.Role;
import io.github.xpax.syllabi.entity.User;
import io.github.xpax.syllabi.entity.dto.InstituteRequest;
import io.github.xpax.syllabi.repo.CourseRepository;
import io.github.xpax.syllabi.repo.InstituteRepository;
import io.github.xpax.syllabi.repo.ProgramRepository;
import io.github.xpax.syllabi.repo.UserRepository;
import io.github.xpax.syllabi.security.JwtTokenUtil;
import io.github.xpax.syllabi.service.UserService;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InstituteControllerIntegrationTest {

    @LocalServerPort
    private int port;

    private String baseUrl;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Autowired
    InstituteRepository instituteRepository;

    private InstituteRequest instituteRequest;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost".concat(":").concat(port+"").concat("/institutes");
        User user = User.builder()
                .username("user1")
                .password("password")
                .roles(new HashSet<>())
                .build();
        userRepository.save(user);

        Role adminRole = new Role();
        adminRole.setAuthority("ROLE_INSTITUTE_ADMIN");
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        User admin = User.builder()
                .username("admin1")
                .password("password")
                .roles(roles)
                .build();
        userRepository.save(admin);

        instituteRequest = new InstituteRequest();
        instituteRequest.setName("Department of Cognitive Science");
    }

    @AfterEach
    void cleanUp() {
        userRepository.deleteAll();
        instituteRepository.deleteAll();
    }

    private String tokenFor(String username) {
        return jwtTokenUtil.generateToken(userService.loadUserToLogin(username));
    }

    private Integer addInstitutes() {
        Institute institute1 = Institute.builder()
                .name("Institute of Philosophy")
                .build();
        instituteRepository.save(institute1);

        Institute institute2 = Institute.builder()
                .name("Institute of Physics")
                .build();
        instituteRepository.save(institute2);

        Institute institute3 = Institute.builder()
                .name("Department of Computer Science")
                .build();
        Integer instituteId = instituteRepository.save(institute3).getId();

        for(int i = 4; i<=25; i++) {
            Institute dummyInstitute = Institute.builder()
                    .name("Dummy Institute #"+i)
                    .build();
            instituteRepository.save(dummyInstitute);
        }

        return instituteId;
    }

    @Test
    void shouldRespondWith401ToGetAllInstitutesRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithInstitutesPageAndDefaultPaginationToGetAllInstitutesRequest() {
        addInstitutes();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(20))
                .body("content[0].name", equalTo("Institute of Philosophy"))
                .body("content[1].name", equalTo("Institute of Physics"))
                .body("content[2].name", equalTo("Department of Computer Science"))
                .body("numberOfElements", equalTo(20));
    }

    @Test
    void shouldRespondWithInstitutesPageAndCustomPaginationToGetAllInstitutesRequest() {
        addInstitutes();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .queryParam("page", 3)
                .queryParam("size", 5)
                .when()
                .get(baseUrl)
                .then()
                .statusCode(OK.value())
                .body("content", hasSize(5))
                .body("content[0].name", equalTo("Dummy Institute #16"))
                .body("numberOfElements", equalTo(5));
    }

    @Test
    void shouldRespondWith401ToDeleteInstituteRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .delete(baseUrl + "/{instituteId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToDeleteInstituteRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .delete(baseUrl + "/{instituteId}", 2)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldDeleteInstitute() {
        Integer id = addInstitutes();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{instituteId}", id)
                .then()
                .statusCode(OK.value());

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{instituteId}", id)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith404IfInstituteToDeleteNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("admin1"))
                .when()
                .delete(baseUrl + "/{instituteId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToGetInstituteRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .get(baseUrl + "/{instituteId}", 2)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWithInstitute() {
        Integer id = addInstitutes();
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{instituteId}", id)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Department of Computer Science"));
    }

    @Test
    void shouldRespondWith404IfInstituteNotFound() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{instituteId}", 404)
                .then()
                .statusCode(NOT_FOUND.value());
    }

    @Test
    void shouldRespondWith401ToAddInstituteRequestIfUserUnauthorized() {
        given()
                .log()
                .uri()
                .when()
                .post(baseUrl)
                .then()
                .statusCode(UNAUTHORIZED.value());
    }

    @Test
    void shouldRespondWith403ToAddInstituteRequestIfNotAdmin() {
        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .contentType(ContentType.JSON)
                .body(instituteRequest)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(FORBIDDEN.value());
    }

    @Test
    void shouldRespondWithAddedInstitute() {
        addInstitutes();
        Integer addedInstituteId = given()
                .log()
                .uri()
                .log()
                .body()
                .auth()
                .oauth2(tokenFor("admin1"))
                .contentType(ContentType.JSON)
                .body(instituteRequest)
                .when()
                .post(baseUrl)
                .then()
                .statusCode(CREATED.value())
                .body("name", equalTo("Department of Cognitive Science"))
                .extract()
                .jsonPath()
                .getInt("id");

        given()
                .log()
                .uri()
                .auth()
                .oauth2(tokenFor("user1"))
                .when()
                .get(baseUrl + "/{instituteId}", addedInstituteId)
                .then()
                .statusCode(OK.value())
                .body("name", equalTo("Department of Cognitive Science"));
    }
}
