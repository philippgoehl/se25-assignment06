package de.unibayreuth.se.campuscoffee.data.mapper;

import de.unibayreuth.se.campuscoffee.data.persistence.UserEntity;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface UserEntityMapper {
    User fromEntity(UserEntity source);
    UserEntity toEntity(User source);
}
