package com.TaiNguyen.ACBBank;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication

@OpenAPIDefinition(
		info = @Info(
			title = "AuthController OPEN API",
				version = "1.0.0",
				description = "Auth OPEN API document"
		),
		servers = @Server(
				url = "http://localhost:2000",
				description = "Auth OPEN API url"

		)
)
//@EnableEurekaServer
@EnableDiscoveryClient
public class AcbBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcbBankApplication.class, args);
	}

}
