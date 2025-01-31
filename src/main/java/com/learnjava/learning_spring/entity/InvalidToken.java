package com.learnjava.learning_spring.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class InvalidToken {
    @Id
    String id;
    Date expiryTime;
}
