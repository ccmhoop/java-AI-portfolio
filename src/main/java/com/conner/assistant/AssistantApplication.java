package com.conner.assistant;

import java.util.HashSet;
import java.util.Set;

import com.conner.assistant.models.UserInfo;
import com.conner.assistant.models.UserRole;
import com.conner.assistant.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.conner.assistant.repository.UserRepository;

@SpringBootApplication
public class AssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(AssistantApplication.class, args);
    }

    @Bean
    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            if (roleRepository.findByAuthority("ADMIN").isPresent()) return;
            UserRole adminRole = new UserRole("ADMIN");
            UserRole userRole = new UserRole("USER");

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            Set<UserRole> admin = new HashSet<>();
            admin.add(adminRole);
            Set<UserRole> user = new HashSet<>();
            user.add(userRole);

            userRepository.save(new UserInfo(
                    1L, "admin", passwordEncoder.encode("password"), admin));
            userRepository.save( new UserInfo(
                    2L, "user", passwordEncoder.encode("password"), user));
        };
    }
}

