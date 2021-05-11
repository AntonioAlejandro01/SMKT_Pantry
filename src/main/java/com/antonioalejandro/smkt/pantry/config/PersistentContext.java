package com.antonioalejandro.smkt.pantry.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.antonioalejandro.smkt.pantry.db.PantryDatabaseImpl;
import com.antonioalejandro.smkt.pantry.service.TokenService;
import com.antonioalejandro.smkt.pantry.service.impl.TokenServiceImpl;

/**
 * The Class PersistentContext.
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @version 1.0.0
 * @apiNote Configuration class that supply the needed beans
 */
@Configuration
public class PersistentContext {

	@Value("${mongodb.connection}")
	private String connectionString;

	@Value("${mongodb.database.name}")
	private String databaseName;

	@Value("${mongodb.database.schema}")
	private String databaseSchema;

	/**
	 * Gets the token service.
	 *
	 * @return {@link TokenService}
	 */
	@Bean
	public TokenService getTokenService() {
		return new TokenServiceImpl();
	}

	/**
	 * MongoDB Bean
	 * 
	 * @return {@link PantryDatabaseImpl}
	 */
	@Bean
	public PantryDatabaseImpl pantryDatabaseImpl() {
		return new PantryDatabaseImpl(connectionString, databaseName, databaseSchema);
	}

}
