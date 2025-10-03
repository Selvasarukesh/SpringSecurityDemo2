package com.tungstn.SpringSecurityDemo2.config;

import com.tungstn.SpringSecurityDemo2.security.jwtFilter;
import com.tungstn.SpringSecurityDemo2.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class UserConfig {

    @Autowired
    private MyUserDetailService userDetailsService;

    @Autowired
    private jwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no login, no jwt)
                        .requestMatchers(
                                "/h2-console/**",
                                "/addUser/**",
                                "/greet/**",
                                "/login/**",
                                "/oauth2/authorization/**",
                                "/login/oauth2/code/**",

                                // Swagger/OpenAPI
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // Protected endpoints
                        .requestMatchers("/getUsers/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // enable OAuth2 login only for protected endpoints
                .oauth2Login(Customizer.withDefaults())
                // stateless session (for JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // allow frames for H2 console
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                // register our JWT filter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(9));
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(List.of(authProvider()));
    }
}
