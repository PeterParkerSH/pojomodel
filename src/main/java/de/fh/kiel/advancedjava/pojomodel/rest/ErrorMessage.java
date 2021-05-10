package de.fh.kiel.advancedjava.pojomodel.rest;


import lombok.Data;

@Data
public class ErrorMessage {

    private String timestamp;
    private String status;
    private String errorMessage;

}