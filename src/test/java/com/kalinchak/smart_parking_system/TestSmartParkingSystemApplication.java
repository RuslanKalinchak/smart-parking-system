package com.kalinchak.smart_parking_system;

import org.springframework.boot.SpringApplication;

public class TestSmartParkingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.from(SmartParkingSystemApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
