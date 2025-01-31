package com.learnjava.learning_spring.service;


import com.learnjava.learning_spring.dto.request.PermissionDTO;
import com.learnjava.learning_spring.dto.response.PermissionResponse;
import com.learnjava.learning_spring.entity.Permission;
import com.learnjava.learning_spring.exception.AppException;
import com.learnjava.learning_spring.exception.ErrorCode;
import com.learnjava.learning_spring.mapper.PermissionMapper;
import com.learnjava.learning_spring.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;


    public PermissionResponse createPermission(PermissionDTO request) {
        var isExisted = permissionRepository.findById(request.getName());
        if (isExisted.isPresent()) {
            throw  new AppException(ErrorCode.ROLE_IS_EXISTED);
        }
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

   public List<PermissionResponse> getAllPermissions() {
        var permissions = permissionRepository.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }
   public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }
}
