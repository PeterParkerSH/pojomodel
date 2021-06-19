package de.fh.kiel.advancedjava.pojoapplication.rest.controller;

import de.fh.kiel.advancedjava.pojoapplication.rest.restmodel.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class ExceptionHandlingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlingController.class);

    /**
     * This is the exception handler for MediaType.APPLICATION_JSON
     */
    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ErrorMessage>
                        responseStatusErrorHandler(HttpServletRequest req, ResponseStatusException e) {
        ErrorMessage emsg = new ErrorMessage();
        emsg.setTimestamp(new Date().toString());
        emsg.setStatus(String.valueOf(e.getStatus().value()));
        emsg.setMessage(e.getMessage());
        LOGGER.error("Exception Occurred : {}", req);
        return ResponseEntity.status(e.getStatus()).contentType(MediaType.APPLICATION_JSON).body(emsg);
    }
}
