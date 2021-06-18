package de.fh.kiel.advancedjava.pojoapplication.rest.restmodel;


import lombok.Data;

@Data
public class ErrorMessage {

    private String timestamp;
    private String status;
    private String message;

}