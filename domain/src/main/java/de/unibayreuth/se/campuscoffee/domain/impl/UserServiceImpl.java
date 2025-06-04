package de.unibayreuth.se.campuscoffee.domain.impl;

import de.unibayreuth.se.campuscoffee.domain.exceptions.UserNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import de.unibayreuth.se.campuscoffee.domain.ports.UserDataService;
import de.unibayreuth.se.campuscoffee.domain.ports.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDataService userDataService;

    @Override
    public void clear() {
        userDataService.clear();
    }

    @Override
    @NonNull
    public List<User> getAll() {
        return userDataService.getAll();
    }

    @Override
    @NonNull
    public User getById(@NonNull Long id) throws UserNotFoundException {
        return userDataService.getById(id);
    }

    @Override
    @NonNull
    public User getByLoginName(@NonNull String loginName) throws UserNotFoundException {
        return userDataService.getByLoginName(loginName);
    }

    @Override
    @NonNull
    public User upsert(@NonNull User user) throws UserNotFoundException {
        if (user.getId() != null) {
            userDataService.getById(user.getId()); // throws exception if user does not exist
        }
        return userDataService.upsert(user);
    }
}
