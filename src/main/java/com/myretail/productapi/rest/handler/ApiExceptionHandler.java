package com.myretail.productapi.rest.handler;

import com.myretail.productapi.rest.dto.ErrorDTO;
import com.myretail.productapi.rest.dto.ErrorsDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.myretail.productapi.framework.domain.entities.ProcessingReport.Event.ERROR_WHILE_PROCESSING_REQUEST;

@ControllerAdvice
@RestController
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorsDTO> handleExceptions(Exception ex, WebRequest request) {
        ErrorsDTO errorsDTO = new ErrorsDTO();
        errorsDTO.addError(new ErrorDTO(ERROR_WHILE_PROCESSING_REQUEST, ex.getMessage()));

        return new ResponseEntity<>(errorsDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}