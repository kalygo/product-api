package com.myretail.productapi.framework.fetchers;

import com.google.common.collect.Lists;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.service.ProductByTcinRestService;
import com.myretail.productapi.models.ProductByTcin;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.myretail.productapi.framework.domain.entities.ProcessingReport.Event.ERR_PRODUCT_DATA_NOT_AVAILABLE;


public class ProductByTcinFetcher implements Fetcher<Long, ProductByTcin> {

    private ProductByTcinRestService productByTcinRestService;
    private EntityMapper entityMapper;

    public ProductByTcinFetcher(ProductByTcinRestService productByTcinRestService, EntityMapper entityMapper) {
        this.productByTcinRestService = productByTcinRestService;
        this.entityMapper = entityMapper;
    }

    @Override
    public EntityMapper getEntityMapper() {
        return this.entityMapper;
    }

    @Override
    public Map<Long, ProductByTcin> fetchData(Iterable<Long> tcins) {
        Map<Long, ProductByTcin> resultset = new LinkedHashMap<>();
        if(null!=tcins) {
            Iterable<ProductByTcin> products = productByTcinRestService.getProductsByTcin(Lists.newArrayList(tcins), true);

            for (ProductByTcin pr : products) {
                if(null!=pr) {
                    resultset.put(pr.getTcin(), pr);
                }
            }
        }
        return resultset;
    }

    public static class DefaultMapper implements EntityMapper<Long, Product, ProductByTcin> {

        @Override
        public void map(@NotNull Map<Long, Product> destinationById, Map<Long, ProductByTcin> sourceById) {
            if(null!= sourceById) {
                for (Long tcin : destinationById.keySet()) {
                    Product target = destinationById.get(tcin);

                    if(sourceById.containsKey(tcin)) {
                        ProductByTcin src = sourceById.get(tcin);
                        target.setName(src.getName());
                        target.setTcin(src.getTcin());
                    } else {
                        target.getProcessingReport().addEvent(ERR_PRODUCT_DATA_NOT_AVAILABLE);
                    }
                }
            }
        }
    }
}
