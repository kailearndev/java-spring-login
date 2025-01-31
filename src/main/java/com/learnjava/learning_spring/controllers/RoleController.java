package com.learnjava.learning_spring.controllers;


import com.learnjava.learning_spring.dto.request.PermissionDTO;
import com.learnjava.learning_spring.dto.request.RoleDTO;
import com.learnjava.learning_spring.dto.response.ApiResponse;
import com.learnjava.learning_spring.dto.response.PermissionResponse;
import com.learnjava.learning_spring.dto.response.RoleResponse;
import com.learnjava.learning_spring.exception.SuccessCode;
import com.learnjava.learning_spring.mapper.RoleMapper;
import com.learnjava.learning_spring.service.PermissionService;
import com.learnjava.learning_spring.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleDTO request) {
        return ApiResponse.<RoleResponse>builder()
                .code(SuccessCode.SUCCESS.getCode())
                .data(roleService.createRole(request))
                .build();

    }
    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .code(SuccessCode.SUCCESS.getCode())
                .data(roleService.getAllRoles())
                .build();
    }

    @DeleteMapping("/{roleId}")
    ApiResponse<Void> deleteRole(@PathVariable("roleId") String role) {
        roleService.deleteRole(role);
        return  ApiResponse.<Void>builder()
                .code(SuccessCode.SUCCESS.getCode())
                .build();
    }

}
