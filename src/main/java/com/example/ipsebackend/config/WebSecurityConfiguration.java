package com.example.ipsebackend.config;

import com.example.ipsebackend.enums.UserRole;
import com.example.ipsebackend.service.jwt.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/swagger-ui.html").permitAll() // Allow access to Swagger UI
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll() // Allow access to H2 console
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/admin/**").hasAnyAuthority(UserRole.ADMIN.name())
                        .requestMatchers("/api/employeRetraite/**").hasAnyAuthority(UserRole.EmpRetraite.name())
                        .requestMatchers("/api/employeActif/**").hasAnyAuthority(UserRole.EmpActif.name())

                        .requestMatchers("/api/contact/send-email").permitAll()
                        .requestMatchers("/api/Contrat/**").permitAll()
/*

                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyAuthority("ADMIN", "VENDOR")

                        .requestMatchers(HttpMethod.POST, "/api/modems/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/modems/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/modems/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/modems/**").hasAnyAuthority("ADMIN", "VENDOR")

                        .requestMatchers(HttpMethod.POST, "/api/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/products/**").hasAnyAuthority("ADMIN", "VENDOR")

                        .requestMatchers(HttpMethod.POST, "/api/ventes/**").hasAuthority("VENDOR")
                        .requestMatchers(HttpMethod.PUT, "/api/ventes/**").hasAuthority("VENDOR")
                        .requestMatchers(HttpMethod.DELETE, "/api/ventes/**").hasAuthority("VENDOR")
                        .requestMatchers(HttpMethod.GET, "/api/ventes/**").hasAnyAuthority("ADMIN", "VENDOR")
*/


                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        // Correctly disable frame options
        http.headers(headers -> headers.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}