package com.edr.technews;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security Adapter provides security configuration for the
 * rest api
 *
 */
@Configuration
@EnableWebSecurity
public class SecurityAdapter {

  //just make everything accessible

  @Bean
  @Order(Ordered.HIGHEST_PRECEDENCE)
  SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
    throws Exception {
    return http
      .formLogin(f -> f.disable())
      .csrf(c -> c.disable())
      .authorizeHttpRequests(request ->
        request
          //   .requestMatchers(new AntPathRequestMatcher("/user/**"))
          .dispatcherTypeMatchers(DispatcherType.FORWARD)
          .permitAll()
          .anyRequest()
          .permitAll()
      )
      .build();
  }
}
