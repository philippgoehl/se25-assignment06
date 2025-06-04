package de.unibayreuth.se.campuscoffee.systest;

import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import de.unibayreuth.se.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;
import java.util.List;

import de.unibayreuth.se.campuscoffee.TestUtils;
import static org.assertj.core.api.Assertions.assertThat;

public class PosSystemTests extends AbstractSysTest {

    @Test
    void createPos() {
        Pos posToCreate = TestFixtures.getPosListForInsertion().getFirst();
        Pos createdPos = posDtoMapper.toDomain(TestUtils.createPos(List.of(posDtoMapper.fromDomain(posToCreate))).getFirst());

        assertThat(createdPos)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .isEqualTo(posToCreate);
    }

    @Test
    void getAllCreatedPos() {
        List<Pos> createdPosList = TestFixtures.createPos(posService);

        List<Pos> retrievedPos = TestUtils.retrievePos()
                .stream()
                .map(posDtoMapper::toDomain)
                .toList();

        assertThat(retrievedPos)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .containsExactlyInAnyOrderElementsOf(createdPosList);
    }

    @Test
    void getPosById() {
        List<Pos> createdPosList = TestFixtures.createPos(posService);
        Pos createdPos = createdPosList.getFirst();

        Pos retrievedPos = posDtoMapper.toDomain(
                TestUtils.retrievePosById(createdPos.getId())
        );

        assertThat(retrievedPos)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .isEqualTo(createdPos);
    }

    @Test
    void filterPosByName() {
        List<Pos> createdPosList = TestFixtures.createPos(posService);
        Pos createdPos = createdPosList.getFirst();
        String posName = createdPos.getName();
        Pos filteredPos = posDtoMapper.toDomain(TestUtils.retrievePosByName(posName));

        assertThat(filteredPos)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps
                .isEqualTo(createdPos);
    }

    @Test
    void updatePos() {
        List<Pos> createdPosList = TestFixtures.createPos(posService);
        Pos posToUpdate = createdPosList.getFirst();

        // Update fields
        posToUpdate.setName(posToUpdate.getName() + " (Updated)");
        posToUpdate.setDescription("Updated description");

        Pos updatedPos = posDtoMapper.toDomain(TestUtils.updatePos(List.of(posDtoMapper.fromDomain(posToUpdate))).getFirst());

        assertThat(updatedPos)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(posToUpdate);

        // Verify changes persist
        Pos retrievedPos = posDtoMapper.toDomain(TestUtils.retrievePosById(posToUpdate.getId()));

        assertThat(retrievedPos)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(posToUpdate);
    }
}