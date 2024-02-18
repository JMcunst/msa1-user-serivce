package com.example.userserivce;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-service")
@Slf4j
public class UserServiceController {
    Environment env;

    public UserServiceController(Environment env){
        this.env = env;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        log.info("Server port={}", request.getServerPort());

        return String.format("Health Check: %s", env.getProperty("local.server.port"));
    }
}
