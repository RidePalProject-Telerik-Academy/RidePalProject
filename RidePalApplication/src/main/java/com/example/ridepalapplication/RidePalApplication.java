package com.example.ridepalapplication;

import com.example.ridepalapplication.models.Role;
import com.example.ridepalapplication.models.User;
import com.example.ridepalapplication.repositories.RoleRepository;
import com.example.ridepalapplication.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EntityScan(basePackages = "com.example.ridepalapplication.models")
public class RidePalApplication {

    public static void main(String[] args) {
        SpringApplication.run(RidePalApplication.class, args);
    }


//    @Bean
//    CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
//        return args -> {
//            if (roleRepository.findByAuthority("ADMIN").isPresent()) {
//                return;
//            }
//            Role adminRole = roleRepository.save(new Role("ADMIN"));
//            Role userRole = roleRepository.save(new Role("USER"));
//
//            Set<Role> roles = new HashSet<>();
//            roles.add(adminRole);
//            User admin = new User(1,"admin", passwordEncoder.encode("password"), roles);
//            userRepository.save(admin);
//        };
//    }
}
