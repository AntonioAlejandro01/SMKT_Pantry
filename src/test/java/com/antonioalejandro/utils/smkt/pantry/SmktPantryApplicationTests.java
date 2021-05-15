package com.antonioalejandro.utils.smkt.pantry;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.antonioalejandro.smkt.pantry.SmktPantryApplication;

@SpringBootTest(classes = SmktPantryApplicationTests.class)
class SmktPantryApplicationTests {

	@Test
	void test() {
		assertDoesNotThrow(() -> {
			SmktPantryApplication.main(new String[] {});
		});
	}
}
