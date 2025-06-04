package de.unibayreuth.se.campuscoffee.api.mapper;

import de.unibayreuth.se.campuscoffee.api.dtos.PosDto;
import de.unibayreuth.se.campuscoffee.domain.model.Pos;
import org.mapstruct.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Mapper(componentModel = "spring")
@ConditionalOnMissingBean // prevent IntelliJ warning about duplicate beans
public interface PosDtoMapper {
    PosDto fromDomain(Pos source);
    Pos toDomain(PosDto source);
}