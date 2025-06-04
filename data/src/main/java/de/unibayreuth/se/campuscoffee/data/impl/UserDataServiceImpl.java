package de.unibayreuth.se.campuscoffee.data.impl;

import de.unibayreuth.se.campuscoffee.data.mapper.UserEntityMapper;
import de.unibayreuth.se.campuscoffee.data.persistence.*;
import de.unibayreuth.se.campuscoffee.domain.exceptions.PosNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.exceptions.UserNotFoundException;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import de.unibayreuth.se.campuscoffee.domain.ports.UserDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * Implementation of the POS data service that the domain layer provides as a port.
 */
@Service
@RequiredArgsConstructor
class UserDataServiceImpl implements UserDataService {
    private final UserRepository userRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public void clear() {
        userRepository.deleteAllInBatch();
        userRepository.flush();
        userRepository.resetSequence();
    }

    @Override
    @NonNull
    public List<User> getAll() {
        return userRepository.findAll().stream()
                .map(userEntityMapper::fromEntity)
                .toList();
    }

    @Override
    @NonNull
    public User getById(@NonNull Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .map(userEntityMapper::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " does not exist."));
    }

    @Override
    @NonNull
    public User getByLoginName(@NonNull String loginName) throws UserNotFoundException {
        return userRepository.findByLoginName(loginName)
                .map(userEntityMapper::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("User with login name '" + loginName + "' does not exist."));
    }

    @Override
    @NonNull
    public User upsert(@NonNull User user) throws UserNotFoundException {
        if (user.getId() == null) {
            // create new user
            user.setCreatedAt(LocalDateTime.now(ZoneId.of("UTC")));
            user.setUpdatedAt(user.getCreatedAt());
            return userEntityMapper.fromEntity(userRepository.saveAndFlush(userEntityMapper.toEntity(user)));
        }

        // update existing user
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new PosNotFoundException("User with ID " + user.getId() + " does not exist."));
        // use mapper to be able to use the house number conversion logic
        UserEntity mappedUserEntity = userEntityMapper.toEntity(user);
        // update fields
        userEntity.setLoginName(mappedUserEntity.getLoginName());
        userEntity.setEmailAddress(mappedUserEntity.getEmailAddress());
        userEntity.setFirstName(mappedUserEntity.getFirstName());
        userEntity.setLastName(mappedUserEntity.getLastName());
        userEntity.setUpdatedAt(LocalDateTime.now());

        return userEntityMapper.fromEntity(userRepository.saveAndFlush(userEntity));

    }
}
