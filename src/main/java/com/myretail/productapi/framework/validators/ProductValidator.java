package com.myretail.productapi.framework.validators;

import com.myretail.productapi.framework.domain.entities.ProcessingReport;
import com.myretail.productapi.rest.dto.ErrorDTO;
import com.myretail.productapi.rest.dto.ErrorsDTO;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.tuple.Pair;

public class ProductValidator implements Validator<Pair<Pair<Long, ProductDTO>, ErrorsDTO>> {

    @Override
    public boolean validate(Pair<Pair<Long, ProductDTO>, ErrorsDTO> inputDataAndErrorsDTOPair) {
        ErrorsDTO errorsDTO = inputDataAndErrorsDTOPair.getValue();

        ProductDTO productDTO = inputDataAndErrorsDTOPair.getKey().getValue();
        //TODO: Validation to be done for product validity

        return ! errorsDTO.hasErrors();
    }
}
