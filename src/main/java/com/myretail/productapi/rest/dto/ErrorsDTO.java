package com.myretail.productapi.rest.dto;

import java.util.LinkedList;
import java.util.List;

public class ErrorsDTO {
    private List<ErrorDTO> errors;

    public void addError(ErrorDTO errorDTO){
        if(null==this.errors){
            this.errors = new LinkedList<>();
        }

        this.errors.add(errorDTO);
    }

    public void addErrors(List<ErrorDTO> errorDTOs){
        if(null==this.errors){
            this.errors = new LinkedList<>();
        }

        this.errors.addAll(errorDTOs);
    }

    public List<ErrorDTO> getErrors() {
        return errors;
    }
    public boolean hasErrors(){
        return getErrors()!=null && getErrors().size() > 0;
    }
}
