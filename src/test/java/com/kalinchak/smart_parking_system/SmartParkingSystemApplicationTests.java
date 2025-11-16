package com.kalinchak.smart_parking_system;

import com.kalinchak.smart_parking_system.controller.configuration.TestContainersConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("integration-test")
@ContextConfiguration(classes = TestContainersConfig.class)
class SmartParkingSystemApplicationTests {

	@Test
	void contextLoads() {
	}

}
