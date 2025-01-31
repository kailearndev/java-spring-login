package com.learnjava.learning_spring.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserResponse {
    String username;
    String id;
    String name;
    LocalDateTime createdAt;

    Set<RoleResponse> roles;
}
