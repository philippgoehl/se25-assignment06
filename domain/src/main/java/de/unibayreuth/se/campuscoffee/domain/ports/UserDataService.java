package de.unibayreuth.se.campuscoffee.domain.ports;

import de.unibayreuth.se.campuscoffee.domain.exceptions.UserNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface for the implementation of the user data service that the domain layer provides as a port.
 */
public interface UserDataService {
    void clear();
    @NonNull
    List<User> getAll();
    User getById(@NonNull Long id) throws UserNotFoundException;
    User getByLoginName(@NonNull String loginName) throws UserNotFoundException;
    @NonNull
    User upsert(@NonNull User user) throws UserNotFoundException;
}
