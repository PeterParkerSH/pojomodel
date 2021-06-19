package de.fh.kiel.advancedjava.pojoapplication.binaryreading;

/**
 * Exception during binary reading process
 */
public class BinaryReadingException extends Exception {
    /**
     * Constructor for BinaryReadingException
     * @param msg Exception message string
     */
    public BinaryReadingException(String msg){
        super(msg);
    }
}
