package com.gourav.payflowx.mapper;

import com.gourav.payflowx.dto.request.CreateUserRequest;
import com.gourav.payflowx.dto.response.UserResponse;
import com.gourav.payflowx.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "wallet", ignore = true)
    User toEntity(CreateUserRequest request);

    UserResponse toResponse(User user);
}