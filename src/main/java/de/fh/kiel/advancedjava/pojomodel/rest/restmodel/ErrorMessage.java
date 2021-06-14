package de.fh.kiel.advancedjava.pojomodel.rest.restmodel;


import lombok.Data;

@Data
public class ErrorMessage {

    private String timestamp;
    private String status;
    private String message;

}