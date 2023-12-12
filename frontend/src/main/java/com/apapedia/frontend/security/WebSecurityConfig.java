package com.apapedia.frontend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(requests -> requests
                                                .requestMatchers(new AntPathRequestMatcher("/css/**")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/js/**")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/validate-ticket"))
                                                .permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/logout-sso")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/register")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/login-sso")).permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/api/user/withdraw"))
                                                .permitAll()
                                                .requestMatchers(new AntPathRequestMatcher("/api/user/topup"))
                                                .permitAll()
                                                // .requestMatchers(new AntPathRequestMatcher("/")).permitAll()
                                                .anyRequest().authenticated())
                                // form login ini dihapus agar menuju ke catalogue page
                                .formLogin((form) -> form
                                                .loginPage("/login-sso")
                                                .permitAll()
                                                .defaultSuccessUrl("/catalogue"))
                                .logout((logout) -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login-sso"));
                return http.build();
        }
}
