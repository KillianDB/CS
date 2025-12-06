package com.guilherme.cloudserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class CloudserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudserverApplication.class, args);
	}

}
