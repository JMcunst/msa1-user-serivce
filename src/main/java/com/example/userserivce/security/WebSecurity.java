package com.example.userserivce.security;

import com.example.userserivce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

import java.util.function.Supplier;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {
    private static final String ALLOWED_IP_ADDRESS = "192.168.0.30";
    private static final String SUBNET = "/32";
    private static final IpAddressMatcher ALLOWED_IP_ADDRESS_MATCHER = new IpAddressMatcher(ALLOWED_IP_ADDRESS + SUBNET);
    private static final String IP_CHECK_PATH_PREFIX = "/api/temp";
    private static final String IP_CHECK_PATH_PATTERN = IP_CHECK_PATH_PREFIX + "/**";
    private static final String[] WHITE_LIST = {
            "/welcome",
            "/health-check",
            "/users"
    };

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Environment environment;
    private final ObjectPostProcessor<Object> objectPostProcessor;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> {
                            request.requestMatchers(WHITE_LIST).permitAll();
                            request.requestMatchers(IP_CHECK_PATH_PATTERN).access(this::hasIpAddress)
                                    .anyRequest().authenticated();
                        }
                )
                .headers(header -> header.frameOptions(
                                HeadersConfigurer.FrameOptionsConfig::disable
                        )
                ).addFilter(getAuthenticationFilter());

        return http.build();
    }

    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
        return auth.build();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationManagerBuilder builder = new AuthenticationManagerBuilder(objectPostProcessor);
        //        authenticationFilter.setAuthenticationManager(authenticationManager(builder));
        return new AuthenticationFilter(authenticationManager(builder), userService, environment);
    }

    private AuthorizationDecision hasIpAddress(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        return new AuthorizationDecision(
                !(authentication.get() instanceof AnonymousAuthenticationToken)
                        && ALLOWED_IP_ADDRESS_MATCHER.matches(object.getRequest()
                ));
    }
}
