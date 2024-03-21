package com.codecool.solarwatch.security;

import com.codecool.solarwatch.model.entity.Role;
import com.codecool.solarwatch.security.jwt.AuthEntryPointJwt;
import com.codecool.solarwatch.security.jwt.AuthTokenFilter;
import com.codecool.solarwatch.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    private final AuthEntryPointJwt unauthorizedHandler;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;


    @Autowired
    public WebSecurityConfig(AuthEntryPointJwt unauthorizedHandler, UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.unauthorizedHandler = unauthorizedHandler;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.GET, "/api/sunrise").authenticated()
                        .requestMatchers("/api/registration").permitAll()
                        .requestMatchers("/api/signin").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sunset").authenticated()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    public AuthTokenFilter authenticationJwtTokenFilter(){
        return new AuthTokenFilter(userDetailsService, jwtUtils);
    }
}
