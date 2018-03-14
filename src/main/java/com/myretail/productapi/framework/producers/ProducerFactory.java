package com.myretail.productapi.framework.producers;

import com.myretail.productapi.framework.assembler.IdBasedAssembler;
import com.myretail.productapi.framework.convertors.ProductToProductDTOConvertor;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.fetchers.FetchedResultRow;
import com.myretail.productapi.framework.fetchers.Fetcher;
import com.myretail.productapi.framework.fetchers.IdBasedCombinedFetcher;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ProducerFactory {

    public Producer<ProductDTO> newProductProducer(Set<Long> tcins, Set<Fetcher<Long, FetchedResultRow>> fetcherSet) {
        return new IdBasedProducer<>(tcins,
                                        new IdBasedCombinedFetcher<>(fetcherSet, 10),
                                        (tcin) -> new Product(tcin),
                                        new IdBasedAssembler<>(),
                                        new ProductToProductDTOConvertor());
    }

}
