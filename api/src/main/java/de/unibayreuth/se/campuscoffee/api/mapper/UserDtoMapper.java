package de.unibayreuth.se.campuscoffee.api.mapper;

import de.unibayreuth.se.campuscoffee.api.dtos.UserDto;
import de.unibayreuth.se.campuscoffee.domain.model.User;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface UserDtoMapper {
    UserDto fromDomain(User source);
    User toDomain(UserDto source);
}