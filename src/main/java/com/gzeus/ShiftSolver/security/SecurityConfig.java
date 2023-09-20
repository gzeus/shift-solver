package com.gzeus.ShiftSolver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    // support for JDBC
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource){

        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(config->
                config
                        .requestMatchers(HttpMethod.GET, "/api/shifts", "api/employees/", "api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.GET, "/api/shifts/**", "api/employees/", "api/employees/**").hasRole("EMPLOYEE")
                        .requestMatchers(HttpMethod.POST, "/api/shifts", "api/employees/", "api/employees/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.PUT, "/api/shifts", "api/employees/", "api/employees/**").hasRole("MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/shifts/**", "api/employees/", "api/employees/**").hasRole("ADMIN")

        );

        // use HTTP Basic authentization
        httpSecurity.httpBasic(Customizer.withDefaults());

        // Disable Cross Site Request Forgery (CSFR)
        // in general, not required for stateless REST APIs that use POST, PUT, DELETE and/or PATCH

        httpSecurity.csrf(csrf -> csrf.disable());

        return httpSecurity.build();
    }
}
