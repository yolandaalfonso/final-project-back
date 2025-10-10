package dev.yol.final_project_back.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ Desactiva CSRF (necesario para Postman y pruebas)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> {})
            // Configura autorización
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/register", "/api/login", "/h2-console/**").permitAll() // público
                .anyRequest().authenticated() // el resto protegido
            )
            // Permite que la consola H2 se cargue en un iframe
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
