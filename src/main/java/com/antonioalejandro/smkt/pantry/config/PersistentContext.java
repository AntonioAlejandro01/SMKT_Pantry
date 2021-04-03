package com.antonioalejandro.smkt.pantry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.antonioalejandro.smkt.pantry.service.TokenService;
import com.antonioalejandro.smkt.pantry.service.impl.TokenServiceImpl;

@Configuration
public class PersistentContext {

	/**
	 * Gets the token service.
	 *
	 * @return the token service
	 */
	@Bean
	public TokenService getTokenService() {
		return new TokenServiceImpl();
	}
	
	
}
