package de.fh.kiel.advancedjava.pojoapplication.rest.restmodel;


import lombok.Data;

/**
 * Model for displaying a error message to the user
 */
@Data
public class ErrorMessage {

    private String timestamp;
    private String status;
    private String message;

}