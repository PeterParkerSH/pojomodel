package de.fh.kiel.advancedjava.pojoapplication.rest.exceptions;

/**
 * Exception type for errors during the reading of Pojos from ASM ClassNodes. For more information see
 */
public class ClassHandlingException extends Exception {
    public ClassHandlingException(String msg){
        super(msg);
    }
}
