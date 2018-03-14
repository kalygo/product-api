package com.myretail.productapi.framework.updater;

import com.google.common.collect.Lists;
import com.myretail.productapi.framework.convertors.ProductDTOToProductConvertor;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.persisters.Persister;
import com.myretail.productapi.framework.persisters.ProductCombinedPersister;
import com.myretail.productapi.framework.validators.PriceValidator;
import com.myretail.productapi.framework.validators.ProductValidator;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class UpdaterFactory {

    public ProductUpdater newProductUpdater(Long tcin, ProductDTO productDTO, Set<Persister<Product, Boolean>> persisters){
        return new ProductUpdater(tcin, productDTO,
                Lists.newArrayList(new PriceValidator(), new ProductValidator()),
                new ProductDTOToProductConvertor(),
                new ProductCombinedPersister(persisters, 10));
    }

}
