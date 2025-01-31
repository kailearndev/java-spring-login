package com.learnjava.learning_spring.controllers;


import com.learnjava.learning_spring.dto.request.PermissionDTO;
import com.learnjava.learning_spring.dto.response.ApiResponse;
import com.learnjava.learning_spring.dto.response.PermissionResponse;
import com.learnjava.learning_spring.exception.SuccessCode;
import com.learnjava.learning_spring.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission (@RequestBody PermissionDTO request) {

        return ApiResponse.<PermissionResponse>builder()
                .code(SuccessCode.SUCCESS.getCode())
                .data(permissionService.createPermission(request))
                .build();

    }
    @GetMapping
    ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .code(SuccessCode.SUCCESS.getCode())
                .data(permissionService.getAllPermissions())
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> deletePermission(@PathVariable("permission") String permission) {
        permissionService.deletePermission(permission);
        return  ApiResponse.<Void>builder()
                .code(SuccessCode.SUCCESS.getCode())
                .build();
    }

}
