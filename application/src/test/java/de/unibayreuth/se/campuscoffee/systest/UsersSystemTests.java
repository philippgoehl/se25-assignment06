package de.unibayreuth.se.campuscoffee.systest;

import de.unibayreuth.se.campuscoffee.TestUtils;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import de.unibayreuth.se.campuscoffee.domain.tests.TestFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersSystemTests extends AbstractSysTest {

    @Test
    void createUser() {
        User userToCreate = TestFixtures.getUserListForInsertion().getFirst();
        User createdUser = userDtoMapper.toDomain(TestUtils.createUsers(List.of(userDtoMapper.fromDomain(userToCreate))).getFirst());

        assertThat(createdUser)
                .usingRecursiveComparison()
                .ignoringFields("id", "createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .isEqualTo(userToCreate);
    }

    @Test
    void getAllCreatedUsers() {
        List<User> createdUserList = TestFixtures.createUsers(userService);

        List<User> retrievedUsers = TestUtils.retrieveUsers()
                .stream()
                .map(userDtoMapper::toDomain)
                .toList();

        assertThat(retrievedUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .containsExactlyInAnyOrderElementsOf(createdUserList);
    }

    @Test
    void getUserById() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User createdUser = createdUserList.getFirst();

        User retrievedUser = userDtoMapper.toDomain(
                TestUtils.retrieveUserById(createdUser.getId())
        );

        assertThat(retrievedUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps after conversions
                .isEqualTo(createdUser);
    }

    @Test
    void filterUserByLoginName() {
        List<User> createdUsers = TestFixtures.createUsers(userService);
        User createdUser = createdUsers.getFirst();
        String loginName = createdUser.getLoginName();
        User filteredUser = userDtoMapper.toDomain(TestUtils.retrieveUserByLoginName(loginName));

        assertThat(filteredUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt") // prevent issues due to differing timestamps
                .isEqualTo(createdUser);
    }

    @Test
    void updateUser() {
        List<User> createdUserList = TestFixtures.createUsers(userService);
        User userToUpdate = createdUserList.getFirst();

        // Update fields
        userToUpdate.setLoginName(userToUpdate.getLoginName() + "_updated");
        userToUpdate.setEmailAddress("updated." + userToUpdate.getEmailAddress());

        User updatedUser = userDtoMapper.toDomain(TestUtils.updateUsers(List.of(userDtoMapper.fromDomain(userToUpdate))).getFirst());

        assertThat(updatedUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(userToUpdate);

        // Verify changes persist
        User retrievedUser = userDtoMapper.toDomain(TestUtils.retrieveUserById(userToUpdate.getId()));

        assertThat(retrievedUser)
                .usingRecursiveComparison()
                .ignoringFields("createdAt", "updatedAt")
                .isEqualTo(userToUpdate);
    }
}