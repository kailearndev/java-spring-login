package com.learnjava.learning_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
// use with dto **
public class UserUpdateDTO {
    @NotEmpty(message = "USER_INVALID")
    @Size(min = 3, message = "USER_INVALID")
    String username;
    @NotBlank(message = "PASSWORD_INVALID")
    @Size(min = 6, message = "PASSWORD_INVALID")
    String password;
    @Size(min = 1, message = "NAME_INVALID")
    String name;
    List<String> roles;

}
