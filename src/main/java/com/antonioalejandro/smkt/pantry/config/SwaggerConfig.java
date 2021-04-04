package com.antonioalejandro.smkt.pantry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * The Class SwaggerConfig.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	/**
	 * Users api.
	 *
	 * @return the docket
	 */
	@Bean
	public Docket usersApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(usersApiInfo()).select().paths(PathSelectors.any())
				.apis(RequestHandlerSelectors.basePackage("com.antonioalejandro.smkt.pantry.web")).build();
	}

	/**
	 * Users api info.
	 *
	 * @return the api info
	 */
	private ApiInfo usersApiInfo() {
		return new ApiInfoBuilder().title("SMKT_Pantry").version("1.0").license("MIT License").build();
	}

}