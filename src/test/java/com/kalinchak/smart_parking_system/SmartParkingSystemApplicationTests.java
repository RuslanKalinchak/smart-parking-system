package com.kalinchak.smart_parking_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class SmartParkingSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
