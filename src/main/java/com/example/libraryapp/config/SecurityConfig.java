package com.example.libraryapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register", "/css/**", "/js/**", "/images/**").permitAll() // Autoriser l'accès aux pages de connexion, d'inscription et ressources statiques
                        .requestMatchers("/h2-console/**").permitAll() // Pour H2 Console si utilisé (à désactiver en production)
                        // Accès restreint aux ADMIN pour les actions de modification et ajout
                        .requestMatchers("/livres/ajouter", "/livres/save", "/livres/edit/**", "/livres/update", "/livres/delete/**").hasRole("ADMIN")
                        .requestMatchers("/auteurs/ajouter", "/auteurs/save", "/auteurs/edit/**", "/auteurs/update", "/auteurs/delete/**").hasRole("ADMIN")
                        // Les utilisateurs connectés (ADMIN ou USER) peuvent consulter
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Page de connexion personnalisée
                        .defaultSuccessUrl("/livres", true) // Page après connexion réussie
                        .permitAll() // Autoriser l'accès à tous pour le formulaire de login
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout") // Page après déconnexion réussie
                        .permitAll() // Autoriser l'accès à tous pour la déconnexion
                )
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); // Nécessaire pour H2 console

        return http.build();
    }
}