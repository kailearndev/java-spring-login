package com.learnjava.learning_spring.controllers;

import com.learnjava.learning_spring.dto.request.UserCreateDTO;
import com.learnjava.learning_spring.dto.request.UserUpdateDTO;
import com.learnjava.learning_spring.dto.response.ApiResponse;
import com.learnjava.learning_spring.dto.response.UserResponse;
import com.learnjava.learning_spring.entity.User;
import com.learnjava.learning_spring.exception.SuccessCode;
import com.learnjava.learning_spring.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

    UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreateDTO request) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setCode(SuccessCode.USER_CREATED.getCode());
        apiResponse.setMessage(SuccessCode.USER_CREATED.getMessage());
        userService.createUser(request);
        return apiResponse;
    }
    @GetMapping("/me")
    public ApiResponse<UserResponse> getMe() {
        return  ApiResponse.<UserResponse>builder()
                .code(SuccessCode.USER_CREATED.getCode())
                .data(userService.getMe())
                .build();

    }
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .code(200)
                .data(userService.getAllUsers())
                .message("OK")
                .build();
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String userId) {
        return  ApiResponse.<UserResponse>builder()
                .data(userService.getUserById(userId))
                .code(200)
                .build();
    }

    @PutMapping("/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateDTO request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return "User deleted Successfully " + userId;
    }

}
