package com.example.movie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

// import com.example.board.security.CustomLoginSuccessHandler;

@EnableMethodSecurity
@EnableWebSecurity
@Configuration
public class SecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices)
                        throws Exception {

                http.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/", "/assets/**", "/css/**", "/js/**",
                                                "/upload/**")
                                .permitAll()
                                .requestMatchers("/movie/list", "/movie/read").permitAll()
                                .requestMatchers("/reviews/**", "/upload/display/**").permitAll()
                                .requestMatchers("/member/register").permitAll()
                                .anyRequest().permitAll());
                http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));

                // http.csrf(csrf -> csrf.disable());

                http.formLogin(login -> login.loginPage("/member/login")
                                .defaultSuccessUrl("/")
                                .permitAll());

                http.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
                                .logoutSuccessUrl("/"));

                // http.rememberMe(remember -> remember.rememberMeServices(rememberMeServices));

                return http.build();
        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }

        // @Bean
        // CustomLoginSuccessHandler successHandler() {
        // return new CustomLoginSuccessHandler();
        // }

        @Bean
        RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
                RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
                TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("mykey",
                                userDetailsService,
                                encodingAlgorithm);
                rememberMeServices.setMatchingAlgorithm((RememberMeTokenAlgorithm.MD5));
                rememberMeServices.setTokenValiditySeconds(60 * 60 * 24 * 7);
                return rememberMeServices;
        }

        // DB 연동 아직 할 수 없어서 메모리 상에 가상의 USER 를 인메모리 시킨 것
        // @Bean
        // UserDetailsService users() {
        // UserDetails user = User.builder()
        // .username("user")
        // .password("{bcrypt}$2a$10$bpYgqcU8IDSIbVzyOXFV/.8XwD79A8NxPEMPJ18J4xrY9dx/IRoRW")
        // .roles("USER") // => ROLE_권한명
        // .build();

        // UserDetails admin = User.builder()
        // .username("admin")
        // .password("{bcrypt}$2a$10$bpYgqcU8IDSIbVzyOXFV/.8XwD79A8NxPEMPJ18J4xrY9dx/IRoRW")
        // .roles("USER", "ADMIN") // => ROLE_권한명
        // .build();
        // return new InMemoryUserDetailsManager(user, admin);
        // }

}
