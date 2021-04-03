package com.antonioalejandro.smkt.pantry.model;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.ToString;

@ToString
public class ErrorService extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final HttpStatus status;

	private final String message;

	private final long timestamp;
	private static final String JSON_TEMPLATE = "{\"timestamp\":%d,\"status\":%d,\"message\":\"%s\"}";

	public ErrorService(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = new Date().getTime();

	}

	private String getJSON() {
		return String.format(JSON_TEMPLATE, timestamp, status.value(), message);
	}

	public static final ResponseEntity<String> getReponse(ErrorService error) {
		return ResponseEntity.status(error.status).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(error.getJSON());
	}

}
