package com.example.libraryapp.config;

import com.example.libraryapp.entity.AppUser;
import com.example.libraryapp.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()
                    && userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                AppUser admin = new AppUser(
                        "admin",
                        "admin@gmail.com",
                        passwordEncoder.encode("admin123"),
                        "ADMIN",
                        true
                );
                userRepository.save(admin);
                System.out.println("Compte ADMIN 'admin@gmail.com' créé avec le mot de passe 'admin123'");
            }
        };
    }
}
