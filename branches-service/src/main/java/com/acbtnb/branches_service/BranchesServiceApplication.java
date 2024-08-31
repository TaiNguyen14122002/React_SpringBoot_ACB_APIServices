package com.acbtnb.branches_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BranchesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BranchesServiceApplication.class, args);
	}

}
