package com.zsw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication(scanBasePackages = "com.zsw.**")
@EnableEurekaClient
@EnableDiscoveryClient
public class UserServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServicesApplication.class, args);
	}

}
