package com.gourav.payflowx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PayflowxApplication {

	public static void main(String[] args) {
		SpringApplication.run(PayflowxApplication.class, args);
	}

}
