package de.unibayreuth.se.campuscoffee.acctest;

import de.unibayreuth.se.campuscoffee.domain.ports.PosService;
import de.unibayreuth.se.campuscoffee.api.dtos.PosDto;
import de.unibayreuth.se.campuscoffee.domain.model.CampusType;
import de.unibayreuth.se.campuscoffee.domain.model.PosType;
import io.cucumber.java.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Map;

import static de.unibayreuth.se.campuscoffee.TestUtils.*;
import static de.unibayreuth.se.campuscoffee.TestUtils.configurePostgresContainers;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Step definitions for the POS Cucumber tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@CucumberContextConfiguration
public class CucumberPosSteps {
    static final PostgreSQLContainer<?> postgresContainer = getPostgresContainer();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        configurePostgresContainers(registry, postgresContainer);
    }

    @Autowired
    protected PosService posService;

    @LocalServerPort
    private Integer port;

    @BeforeAll
    public static void beforeAll() {
        postgresContainer.start();
    }

    @AfterAll
    public static void afterAll() {
        postgresContainer.stop();
    }

    @Before
    public void beforeEach() {
        posService.clear();
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @After
    public void afterEach() {
        posService.clear();
    }

    private List<PosDto> createdPosList;
    private PosDto updatedPos;

    @DataTableType
    public PosDto toPosDto(Map<String,String> row) {
        return PosDto.builder()
                .name(row.get("name"))
                .description(row.get("description"))
                .type(PosType.valueOf(row.get("type")))
                .campus(CampusType.valueOf(row.get("campus")))
                .street(row.get("street"))
                .houseNumber(row.get("houseNumber"))
                .postalCode(Integer.parseInt(row.get("postalCode")))
                .city(row.get("city"))
                .build();
    }

    // Given -----------------------------------------------------------------------

    @Given("an empty POS list")
    public void anEmptyPosList() {
        List<PosDto> retrievedPosList = retrievePos();
        assertThat(retrievedPosList).isEmpty();
    }

    @Given("a POS list with the following elements")
    public void aPosListWithTheFollowingElements(List<PosDto> posList) {
        List<PosDto> createdPosList = createPos(posList);
        assertThat(createdPosList).hasSize(posList.size());
    }

    // When -----------------------------------------------------------------------

    @When("I insert POS with the following elements")
    public void iInsertPosWithTheFollowingValues(List<PosDto> posList) {
        createdPosList = createPos(posList);
        assertThat(createdPosList).size().isEqualTo(posList.size());
    }

    @When("I update the description of the POS with name {string} to {string}")
    public void iUpdateTheDescriptionOfThePosWithNameTo(String name, String description) {
        PosDto posToUpdate = retrievePosByName(name);
        updatedPos = posToUpdate
                .toBuilder()
                .description(description)
                .build();
        updatePos(List.of(updatedPos)).getFirst();
    }

    // Then -----------------------------------------------------------------------

    @Then("the POS list should contain the same elements in the same order")
    public void thePosListShouldContainTheSameElementsInTheSameOrder() {
        List<PosDto> retrievedPosList = retrievePos();
        assertThat(retrievedPosList)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "createdAt", "updatedAt")
                .containsExactlyInAnyOrderElementsOf(createdPosList);
    }

    @Then("the updated POS should have the new description")
    public void theUpdatedPosShouldHaveTheNewDescription() {
        PosDto retrievedPos = retrievePosById(updatedPos.getId());
        assertThat(retrievedPos.getDescription()).isEqualTo(updatedPos.getDescription());
    }
}
