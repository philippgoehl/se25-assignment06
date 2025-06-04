package de.unibayreuth.se.campuscoffee.systest;

import de.unibayreuth.se.campuscoffee.api.mapper.PosDtoMapper;
import de.unibayreuth.se.campuscoffee.api.mapper.UserDtoMapper;
import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
import de.unibayreuth.se.campuscoffee.domain.ports.UserService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import static de.unibayreuth.se.campuscoffee.TestUtils.configurePostgresContainers;
import static de.unibayreuth.se.campuscoffee.TestUtils.getPostgresContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractSysTest {
    protected static final PostgreSQLContainer<?> postgresContainer;

    static {
        // share the same testcontainers instance across all system tests
        postgresContainer = getPostgresContainer();
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        configurePostgresContainers(registry, postgresContainer);
    }

    @Autowired
    protected PosService posService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected PosDtoMapper posDtoMapper;

    @Autowired
    protected UserDtoMapper userDtoMapper;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void beforeEach() {
        posService.clear();
        userService.clear();
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void afterEach() {
        posService.clear();
        userService.clear();
    }
}