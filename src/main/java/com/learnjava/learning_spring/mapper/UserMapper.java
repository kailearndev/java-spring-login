package com.learnjava.learning_spring.mapper;
import com.learnjava.learning_spring.dto.request.UserCreateDTO;
import com.learnjava.learning_spring.dto.request.UserUpdateDTO;
import com.learnjava.learning_spring.dto.response.UserResponse;
import com.learnjava.learning_spring.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toUser(UserCreateDTO createDTO);


    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateDTO updateDTO);
}
