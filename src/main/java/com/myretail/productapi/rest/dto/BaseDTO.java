package com.myretail.productapi.rest.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseDTO {

    private ErrorsDTO errorsDTO = null;

    public void addError(ErrorDTO errorDTO){
        if(null==errorDTO){
            return;
        }
        if(this.errorsDTO==null) {
            this.errorsDTO = new ErrorsDTO();
        }

        errorsDTO.addError(errorDTO);
    }

    public void addErrors(List<ErrorDTO> errorDTOs){
        if(null==errorDTOs || errorDTOs.isEmpty()){
            return;
        }

        if(null==this.errorsDTO) {
            this.errorsDTO = new ErrorsDTO();
        }

        errorsDTO.addErrors(errorDTOs);
    }

    public ErrorsDTO getErrorsDTO() {
        return errorsDTO;
    }
}
