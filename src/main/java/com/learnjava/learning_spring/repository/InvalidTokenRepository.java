package com.learnjava.learning_spring.repository;

import com.learnjava.learning_spring.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
}
