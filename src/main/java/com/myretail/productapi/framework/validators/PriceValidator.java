package com.myretail.productapi.framework.validators;

import com.myretail.productapi.framework.domain.entities.ProcessingReport;
import com.myretail.productapi.rest.dto.ErrorDTO;
import com.myretail.productapi.rest.dto.ErrorsDTO;
import com.myretail.productapi.rest.dto.PriceDTO;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.apache.commons.lang3.tuple.Pair;

public class PriceValidator implements Validator<Pair<Pair<Long, ProductDTO>, ErrorsDTO>> {

    @Override
    public boolean validate(Pair<Pair<Long, ProductDTO>, ErrorsDTO> inputDataAndErrorsDTOPair) {
        ErrorsDTO errorsDTO = inputDataAndErrorsDTOPair.getValue();

        PriceDTO priceDTO = inputDataAndErrorsDTOPair.getKey().getValue().getCurrentPrice();
        if(null!=priceDTO){
            if(priceDTO.getCurrency()==null){
                errorsDTO.addError(new ErrorDTO(ProcessingReport.Event.VALIDATION_ERROR, "currency_price.currency is null"));
            }
        }
        if(null!=priceDTO){
            if(priceDTO.getValue()==null){
                errorsDTO.addError(new ErrorDTO(ProcessingReport.Event.VALIDATION_ERROR, "currency_price.value is null"));
            }
        }
        return ! errorsDTO.hasErrors();
    }
}
