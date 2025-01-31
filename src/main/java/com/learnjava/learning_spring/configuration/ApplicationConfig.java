package com.learnjava.learning_spring.configuration;

import com.learnjava.learning_spring.entity.User;
import com.learnjava.learning_spring.enums.Role;
import com.learnjava.learning_spring.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationConfig {

    PasswordEncoder passwordEncoder;
        @Bean
        ApplicationRunner applicationRunner(UserRepository userRepository) {
            return args -> {
                if(userRepository.findByUsername("admin").isEmpty()) {
                    var roles = new HashSet<String>();
                    roles.add(Role.ADMIN.name());
                    User user = User.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("admin"))
//                            .roles(roles)
                            .build();
                    userRepository.save(user);
                    log.warn("Admin user created", user);
                }

            };
        }
}
