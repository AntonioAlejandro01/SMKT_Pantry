package com.antonioalejandro.smkt.pantry.model;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import lombok.ToString;

/**
 * To string.
 *
 * @return the java.lang. string
 */
@ToString
public class ErrorService extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The status. */
	private final HttpStatus status;

	/** The message. */
	private final String message;

	/** The timestamp. */
	private final long timestamp;
	
	/** The Constant JSON_TEMPLATE. */
	private static final String JSON_TEMPLATE = "{\"timestamp\":%d,\"status\":%d,\"message\":\"%s\"}";

	/**
	 * Instantiates a new error service.
	 *
	 * @param status the status
	 * @param message the message
	 */
	public ErrorService(HttpStatus status, String message) {
		super();
		this.status = status;
		this.message = message;
		this.timestamp = new Date().getTime();

	}

	/**
	 * Gets the json.
	 *
	 * @return the json
	 */
	private String getJSON() {
		return String.format(JSON_TEMPLATE, timestamp, status.value(), message);
	}

	/**
	 * Gets the reponse.
	 *
	 * @param error the error
	 * @return the reponse
	 */
	public static final ResponseEntity<String> getReponse(ErrorService error) {
		return ResponseEntity.status(error.status).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.body(error.getJSON());
	}

}
