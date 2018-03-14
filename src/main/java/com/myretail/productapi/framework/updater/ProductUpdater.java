package com.myretail.productapi.framework.updater;

import com.myretail.productapi.framework.convertors.Convertor;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.persisters.CombinedPersister;
import com.myretail.productapi.framework.validators.Validator;
import com.myretail.productapi.rest.dto.ErrorsDTO;
import com.myretail.productapi.rest.dto.PriceDTO;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class ProductUpdater implements Updater<Long, ProductDTO, Product, ErrorsDTO>{

    private Long tcin;
    private ProductDTO productDTO;
    private List<Validator<Pair<Pair<Long, ProductDTO>, ErrorsDTO>>> validators;
    private ErrorsDTO errorsDTO = new ErrorsDTO();
    private Convertor<ProductDTO, Product> convertor;
    private CombinedPersister<Long, Product, Boolean> combinedPersister;

    public ProductUpdater(Long tcin, ProductDTO productDTO, List<Validator<Pair<Pair<Long, ProductDTO>, ErrorsDTO>>> validators, Convertor<ProductDTO, Product> convertor, CombinedPersister<Long, Product, Boolean> combinedPersister){
        this.tcin = tcin;
        this.productDTO = productDTO;
        this.validators = validators;
        this.convertor = convertor;
        this.combinedPersister = combinedPersister;
    }

    @Override
    public boolean validate(Long tcin, ProductDTO productDTO) {
        Pair<Pair<Long, ProductDTO>, ErrorsDTO> dtoAndErrorsPair = Pair.of(Pair.of(tcin, productDTO), errorsDTO);
        validators.stream().forEach(v -> v.validate(dtoAndErrorsPair));
        return ! errorsDTO.hasErrors();
    }

    @Override
    public Product convert(Long tcin, ProductDTO productDTO) {
        Product converted = convertor.convert(productDTO);
        converted.setTcin(tcin);
        return converted;
    }

    @Override
    public void persist(Product product) {
        combinedPersister.persist(product);
    }

    @Override
    public ErrorsDTO update() {
        if(! validate(tcin, productDTO)){
            return errorsDTO;
        }

        Product product = convert(tcin, productDTO);
        persist(product);
        return errorsDTO;
    }
}
