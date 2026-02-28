package com.dwes.security.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dwes.security.entities.Role;
import com.dwes.security.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CORS activo (usa el bean CorsConfigurationSource)
            .cors(cors -> {})
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(request ->
                request
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                    .requestMatchers("/api/v1/auth/**").permitAll()

                    .requestMatchers(HttpMethod.GET, "/api/v1/libros/**")
                        .hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
                    .requestMatchers(HttpMethod.POST, "/api/v1/libros/**")
                        .hasAuthority(Role.ROLE_ADMIN.toString())
                    .requestMatchers(HttpMethod.PUT, "/api/v1/libros/**")
                        .hasAuthority(Role.ROLE_ADMIN.toString())
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/libros/**")
                        .hasAuthority(Role.ROLE_ADMIN.toString())

                    // reglas para el recurso comidas (misma política que libros)
                    .requestMatchers(HttpMethod.GET, "/api/v1/comidas/**")
                        .hasAnyAuthority(Role.ROLE_USER.toString(), Role.ROLE_ADMIN.toString())
                    .requestMatchers(HttpMethod.POST, "/api/v1/comidas/**")
                        .hasAuthority(Role.ROLE_ADMIN.toString())
                    .requestMatchers(HttpMethod.PUT, "/api/v1/comidas/**")
                        .hasAuthority(Role.ROLE_ADMIN.toString())
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/comidas/**")
                        .hasAuthority(Role.ROLE_ADMIN.toString())

                    .requestMatchers("/api/v1/users/**").hasAuthority("ROLE_ADMIN")

                    .anyRequest().authenticated()
            )
            .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        //  Permite acceder desde otro host de la LAN a la Raspberry:
        // - frontend en la misma raspberry: http://localhost:8081 / http://127.0.0.1:8081
        // - frontend desde cualquier PC en LAN: http://192.168.x.x:8081 (y 10.x / 172.16-31.x)
        config.setAllowedOriginPatterns(List.of(
            "http://localhost:8081",
            "http://127.0.0.1:8081",

            "http://192.168.*.*:8081",
            "http://10.*.*.*:8081",
            "http://172.16.*.*:8081",
            "http://172.17.*.*:8081",
            "http://172.18.*.*:8081",
            "http://172.19.*.*:8081",
            "http://172.20.*.*:8081",
            "http://172.21.*.*:8081",
            "http://172.22.*.*:8081",
            "http://172.23.*.*:8081",
            "http://172.24.*.*:8081",
            "http://172.25.*.*:8081",
            "http://172.26.*.*:8081",
            "http://172.27.*.*:8081",
            "http://172.28.*.*:8081",
            "http://172.29.*.*:8081",
            "http://172.30.*.*:8081",
            "http://172.31.*.*:8081"
        ));

        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Para JWT en Authorization normalmente NO hace falta:
        // config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}