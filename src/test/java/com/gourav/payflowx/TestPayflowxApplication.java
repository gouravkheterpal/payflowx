package com.gourav.payflowx;

import org.springframework.boot.SpringApplication;

public class TestPayflowxApplication {

	public static void main(String[] args) {
		SpringApplication.from(PayflowxApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
