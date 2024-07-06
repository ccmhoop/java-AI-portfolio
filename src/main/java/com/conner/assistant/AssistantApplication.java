package com.conner.assistant;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.conner.assistant.models.ApplicationUser;
import com.conner.assistant.models.Role;
import com.conner.assistant.repository.RoleRepository;
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
            Role adminRole = roleRepository.save(new Role("ADMIN"));
            Role userRole = roleRepository.save(new Role("USER"));

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            roles.add(userRole);

            ApplicationUser admin = new ApplicationUser(
                    1L, "admin", passwordEncoder.encode("password"), roles);

            ApplicationUser user = new ApplicationUser(
                    2L, "user", passwordEncoder.encode("password"), roles);

            userRepository.save(admin);
            userRepository.save(user);
        };
    }
}

