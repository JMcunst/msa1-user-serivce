package com.example.userserivce;

import com.netflix.discovery.EurekaClient;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableDiscoveryClient
public class UserSerivceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserSerivceApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private EurekaClient eurekaClient;

	@PreDestroy
	public void unregister() {
		eurekaClient.shutdown();
	}

}
