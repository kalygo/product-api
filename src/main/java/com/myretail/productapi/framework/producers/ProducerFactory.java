package com.myretail.productapi.framework.producers;

import com.google.common.collect.Lists;
import com.myretail.productapi.framework.assembler.IdBasedAssembler;
import com.myretail.productapi.framework.convertors.ProductToProductDTOConvertor;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.fetchers.FetchedResultRow;
import com.myretail.productapi.framework.fetchers.Fetcher;
import com.myretail.productapi.framework.fetchers.IdBasedCombinedFetcher;
import com.myretail.productapi.framework.validators.SimpleValidator;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Component
public class ProducerFactory {

    public static Producer<ProductDTO> newProductProducer(Set<Long> tcins, Set<Fetcher<Long, FetchedResultRow>> fetcherSet) {
        return new IdBasedProducer<>(tcins,
                                        new IdBasedCombinedFetcher<>(fetcherSet, 10),
                                        (tcin) -> new Product(tcin),
                                        new IdBasedAssembler<>(),
                                        new ProductToProductDTOConvertor());
    }

}
