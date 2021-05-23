package com.antonioalejandro.utils.smkt.pantry.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.antonioalejandro.smkt.pantry.model.TokenData;

class TokenDataTest {

	@Test
	void testEmptyConstrcutorsAndToString() throws Exception {
		assertNotNull(new TokenData());
		assertFalse(new TokenData().toString().isBlank());
	}
	
	@Test
	void testConstructorandGetters() throws Exception {
		var tokenData = new TokenData("",List.of(),"",false,1L,List.of(),"","","","","");
		assertEquals("", tokenData.getClientId());
		assertEquals("", tokenData.getEmail());
		assertEquals("", tokenData.getJti());
		assertEquals("", tokenData.getLastname());
		assertEquals("", tokenData.getLastname());
		assertEquals("", tokenData.getUsername());
		assertEquals("", tokenData.getUsernameC());
		
		assertFalse(tokenData.isActive());
		assertTrue(tokenData.getAuthorities().isEmpty());
		assertTrue(tokenData.getScope().isEmpty());
		
	}
}
