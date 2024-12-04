package com.lumen.LumenWorkshopBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.lumen.LumenWorkshopBackend")
@EntityScan("com.lumen.LumenWorkshopBackend")
public class LumenWorkshopBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(LumenWorkshopBackendApplication.class, args);
	}

}
