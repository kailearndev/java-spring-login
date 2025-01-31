package com.learnjava.learning_spring.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class RoleDTO {
    String name;
    String description;
    Set<String> permissions;

}
