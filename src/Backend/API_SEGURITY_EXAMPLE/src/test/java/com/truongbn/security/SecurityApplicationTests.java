package com.truongbn.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.dwes.security.SecurityApplication;

@ActiveProfiles("test")
@SpringBootTest(classes = SecurityApplication.class)
class SecurityApplicationTests {

	@Test
	void contextLoads() {
	}
}