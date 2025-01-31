package com.learnjava.learning_spring.service;

import com.learnjava.learning_spring.dto.request.UserCreateDTO;
import com.learnjava.learning_spring.dto.request.UserUpdateDTO;
import com.learnjava.learning_spring.dto.response.ApiResponse;
import com.learnjava.learning_spring.dto.response.UserResponse;
import com.learnjava.learning_spring.entity.User;
import com.learnjava.learning_spring.enums.Role;
import com.learnjava.learning_spring.exception.AppException;
import com.learnjava.learning_spring.exception.ErrorCode;
import com.learnjava.learning_spring.mapper.UserMapper;
import com.learnjava.learning_spring.repository.RoleRepository;
import com.learnjava.learning_spring.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    public UserResponse createUser(UserCreateDTO request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());
//        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserResponse)
                .toList();  //
    }

    public UserResponse updateUser( String id, UserUpdateDTO request) {
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if ("admin".equals(user.getUsername())) {
            throw new AppException(ErrorCode.IS_ADMIN);
        }
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getMe() {
        var context = SecurityContextHolder.getContext();
        var currentUser = context.getAuthentication().getName();
        User user = userRepository.findByUsername(currentUser).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
    @PostAuthorize("returnObject.username == authentication.name") // check
    public UserResponse getUserById(String id) {

        return userMapper.toUserResponse(userRepository.findById(id).orElseThrow(() ->
                new RuntimeException("User not found")));
    }
}
