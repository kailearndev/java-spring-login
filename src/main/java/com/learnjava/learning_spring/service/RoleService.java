package com.learnjava.learning_spring.service;


import com.learnjava.learning_spring.dto.request.PermissionDTO;
import com.learnjava.learning_spring.dto.request.RoleDTO;
import com.learnjava.learning_spring.dto.response.PermissionResponse;
import com.learnjava.learning_spring.dto.response.RoleResponse;
import com.learnjava.learning_spring.entity.Permission;
import com.learnjava.learning_spring.exception.AppException;
import com.learnjava.learning_spring.exception.ErrorCode;
import com.learnjava.learning_spring.mapper.PermissionMapper;
import com.learnjava.learning_spring.mapper.RoleMapper;
import com.learnjava.learning_spring.repository.PermissionRepository;
import com.learnjava.learning_spring.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleDTO roleDTO) {
        var role = roleMapper.toRole(roleDTO);

        var permissions = permissionRepository.findAllById(roleDTO.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream().map(roleMapper::toRoleResponse)
                .toList();

    }
    public void deleteRole(String roleId) {
         roleRepository.deleteById(roleId);
    }
}
