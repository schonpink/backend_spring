package org.obrancova.librify.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.obrancova.librify.dto.UserDto;
import org.obrancova.librify.model.user.User;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
