package com.learnjava.learning_spring.mapper;

import com.learnjava.learning_spring.dto.request.PermissionDTO;
import com.learnjava.learning_spring.dto.request.RoleDTO;
import com.learnjava.learning_spring.dto.response.PermissionResponse;
import com.learnjava.learning_spring.dto.response.RoleResponse;
import com.learnjava.learning_spring.entity.Permission;
import com.learnjava.learning_spring.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleDTO roleDTO);
    RoleResponse toRoleResponse(Role role);

}
