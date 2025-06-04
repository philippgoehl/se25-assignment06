package de.unibayreuth.se.campuscoffee.domain.ports;

import de.unibayreuth.se.campuscoffee.domain.exceptions.UserNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Interface for the implementation of the user service that the domain layer provides as a port.
 */
public interface UserService {
    void clear();
    @NonNull
    List<User> getAll();
    @NonNull
    User getById(@NonNull Long id) throws UserNotFoundException;
    @NonNull
    User getByLoginName(@NonNull String loginName) throws UserNotFoundException;
    @NonNull
    User upsert(@NonNull User user) throws UserNotFoundException;
}
