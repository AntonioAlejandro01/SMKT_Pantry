package com.antonioalejandro.smkt.pantry.model.exceptions;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Pantry Database Exception class
 * 
 * @author AntonioAlejandro01 - www.antonioalejandro.com
 * @see Exception
 * @version 1.0.0
 * @apiNote Exception to be threw at PantryDatabase
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PantryDatabaseException extends Exception {

    String msg;
    HttpStatus status;
    long timestamp;

    public PantryDatabaseException(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status;
        this.timestamp = Timestamp.from(new Date().toInstant()).getTime();
    }

}
