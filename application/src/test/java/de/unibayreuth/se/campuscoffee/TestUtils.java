package de.unibayreuth.se.campuscoffee;

import de.unibayreuth.se.campuscoffee.api.dtos.PosDto;
import de.unibayreuth.se.campuscoffee.api.dtos.UserDto;
import io.restassured.http.ContentType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static io.restassured.RestAssured.given;

public class TestUtils {
    @SuppressWarnings("resource")
    public static PostgreSQLContainer<?> getPostgresContainer() {
        return new PostgreSQLContainer<>(
                DockerImageName.parse("postgres:17-alpine"))
                .withUsername("postgres")
                .withPassword("postgres")
                .withDatabaseName("postgres")
                .withReuse(true);
    }

    public static void configurePostgresContainers (DynamicPropertyRegistry registry, PostgreSQLContainer<?> postgresContainer) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    public static List<PosDto> retrievePos() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/pos")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("$", PosDto.class)
                .stream()
                .toList();
    }

    public static PosDto retrievePosById(Long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/pos/{id}", id)
                .then()
                .statusCode(200)
                .extract().as(PosDto.class);
    }

    public static PosDto retrievePosByName(String name) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("name", name)
                .when()
                .get("/api/pos/filter")
                .then()
                .statusCode(200)
                .extract().as(PosDto.class);
    }

    public static List<PosDto> createPos(List<PosDto> posList) {
        return posList.stream()
                .map(posDto -> given()
                        .contentType(ContentType.JSON)
                        .body(posDto)
                        .when()
                        .post("/api/pos")
                        .then()
                        .statusCode(200)
                        .extract().as(PosDto.class)
                )
                .toList();
    }

    public static List<PosDto> updatePos(List<PosDto> posList) {
        return posList.stream()
                .map(posDto -> given()
                        .contentType(ContentType.JSON)
                        .body(posDto)
                        .when()
                        .put("/api/pos/{id}", posDto.getId())
                        .then()
                        .statusCode(200)
                        .extract().as(PosDto.class)
                )
                .toList();
    }

    public static List<UserDto> retrieveUsers() {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users")
                .then()
                .statusCode(200)
                .extract().jsonPath().getList("$", UserDto.class)
                .stream()
                .toList();
    }

    public static UserDto retrieveUserById(Long id) {
        return given()
                .contentType(ContentType.JSON)
                .when()
                .get("/api/users/{id}", id)
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);
    }

    public static UserDto retrieveUserByLoginName(String loginName) {
        return given()
                .contentType(ContentType.JSON)
                .queryParam("login_name", loginName)
                .when()
                .get("/api/users/filter")
                .then()
                .statusCode(200)
                .extract().as(UserDto.class);
    }

    public static List<UserDto> createUsers(List<UserDto> userList) {
        return userList.stream()
                .map(userDto -> given()
                        .contentType(ContentType.JSON)
                        .body(userDto)
                        .when()
                        .post("/api/users")
                        .then()
                        .statusCode(200)
                        .extract().as(UserDto.class)
                )
                .toList();
    }

    public static List<UserDto> updateUsers(List<UserDto> userList) {
        return userList.stream()
                .map(userDto -> given()
                        .contentType(ContentType.JSON)
                        .body(userDto)
                        .when()
                        .put("/api/users/{id}", userDto.getId())
                        .then()
                        .statusCode(200)
                        .extract().as(UserDto.class)
                )
                .toList();
    }
}
