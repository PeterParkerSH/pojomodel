package de.fh.kiel.advancedjava.pojomodel.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class ExceptionHandlingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);

    @ExceptionHandler(value = ResponseStatusException.class)
    public ResponseEntity<ErrorMessage>
    responseStatusErrorHandler(HttpServletRequest req, ResponseStatusException e) throws ResponseStatusException {
        ErrorMessage emsg = new ErrorMessage();
        emsg.setTimestamp(new Date().toString());
        emsg.setStatus(String.valueOf(e.getStatus().value()));
        emsg.setErrorMessage(e.getMessage());
        LOGGER.error("Exception Occured : ", req);

        return new ResponseEntity<ErrorMessage>(emsg, e.getStatus());
    }

}

