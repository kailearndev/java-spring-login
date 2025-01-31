package com.learnjava.learning_spring.mapper;

import com.learnjava.learning_spring.dto.request.PermissionDTO;
import com.learnjava.learning_spring.dto.response.PermissionResponse;
import com.learnjava.learning_spring.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionDTO permissionDTO);
    PermissionResponse toPermissionResponse(Permission permission);

}
