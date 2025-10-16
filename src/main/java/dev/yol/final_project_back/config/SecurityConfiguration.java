package dev.yol.final_project_back.config;

import java.util.List;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    /* @Value("${jwt.key}")
    private String key; */

    @Value("${api-endpoint}")
    private String apiEndpoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // ✅ Desactiva CSRF (necesario para Postman y pruebas)
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            // Configura autorización
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    apiEndpoint + "/register",
                    apiEndpoint + "/register/**",
                    apiEndpoint + "/auth/login",
                    apiEndpoint + "/login",
                    "/h2-console/**",
                    "/error" 
                ).permitAll() // público
                .anyRequest().authenticated() // el resto protegido
            )

            // 🔹 Stateless session (JWT ready)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Permite que la consola H2 se cargue en un iframe
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));

            

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key.getBytes()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] bytes = key.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(bytes, 0, bytes.length, "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    } */
}
