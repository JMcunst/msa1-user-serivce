package com.example.userserivce.security;

import com.example.userserivce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> {
                            request.requestMatchers(antMatcher("**")).permitAll();
                            request.requestMatchers(antMatcher("/users/**")).permitAll();
                        }
                ).headers(header -> header.frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::disable
                ));

        return http.build();
    }
    // 1안
//    private static final String[] WHITE_LIST = {
//            "/users/**",
//            "/**"
//    };
//
//    @Bean
//    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
//        http.csrf().disable();
//        http.headers().frameOptions().disable();
//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers(WHITE_LIST).permitAll());
//        return http.build();
//    }
//    최종안
//    private final UserService userService;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final Environment environment;
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> {
//                    request.requestMatchers(antMatcher("/actuator/**")).permitAll();
//                    request.requestMatchers(antMatcher("/**")).permitAll();
//                })
//                //            .headers(header -> header.frameOptions(
//                //                frameOptionsConfig -> frameOptionsConfig.disable()))
//                .apply(new MyCustomSecurity());
//
//        return http.build();
//    }
//
//    public class MyCustomSecurity extends AbstractHttpConfigurer<MyCustomSecurity, HttpSecurity> {
//
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//
//            AuthenticationManager authenticationManager = http.getSharedObject(
//                    AuthenticationManager.class);
//            AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, userService, environment);
//            http.addFilter(authenticationFilter);
//        }
//
//        protected void configure2(AuthenticationManagerBuilder auth) throws Exception {
//            auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
//        }
//
//    }
}
