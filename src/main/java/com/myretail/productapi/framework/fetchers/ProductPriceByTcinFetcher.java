package com.myretail.productapi.framework.fetchers;

import com.myretail.productapi.configurations.CassandraConfigurations.ProductPriceByTcinAccessor;
import com.myretail.productapi.framework.domain.entities.CurrencyType;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.domain.entities.ProductPrice;
import com.myretail.productapi.models.ProductPriceByTcin;
import com.myretail.productapi.models.Status;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.myretail.productapi.framework.domain.entities.BaseEntity.Status.ACTIVE;
import static com.myretail.productapi.framework.domain.entities.BaseEntity.Status.DELETED;
import static com.myretail.productapi.framework.domain.entities.ProcessingReport.Event.WARN_PRODUCT_PRICE_DATA_NOT_AVAILABLE;

public final class ProductPriceByTcinFetcher implements Fetcher<Long, ProductPriceByTcin> {

    private ProductPriceByTcinAccessor productPriceByTcinAccessor;
    private EntityMapper entityMapper;

    public ProductPriceByTcinFetcher(ProductPriceByTcinAccessor productPriceByTcinAccessor, EntityMapper entityMapper){
        this.productPriceByTcinAccessor = productPriceByTcinAccessor;
        this.entityMapper = entityMapper;
    }

    @Override
    public EntityMapper getEntityMapper() {
        return this.entityMapper;
    }

    @Override
    public Map<Long, ProductPriceByTcin> fetchData(Iterable<Long> tcins) {
        Map<Long, ProductPriceByTcin> resultset = new LinkedHashMap<>();

        if(null!=tcins) {
            for (Long tcin : tcins) {
                ProductPriceByTcin price = productPriceByTcinAccessor.getByTcin(tcin).one();
                if (price != null) {
                    resultset.put(tcin, price);
                }
            }
        }

        return resultset;
    }

    public static class DefaultMapper implements EntityMapper<Long, Product, ProductPriceByTcin> {

        @Override
        public void map(@NotNull Map<Long, Product> destinationById, Map<Long, ProductPriceByTcin> sourceById) {
            if(null!= sourceById) {
                for (Long tcin : destinationById.keySet()) {
                    Product destn = destinationById.get(tcin);
                    ProductPriceByTcin src = sourceById.get(tcin);

                    if(null!=src && src.getStatus().equals(Status.ACTIVE)) {

                        ProductPrice productPrice = new ProductPrice(new BigDecimal(src.getPrice().toString()), CurrencyType.valueOf(src.getCurrencyCode()));
                        productPrice.setStatus(Status.DELETED.equals(src.getStatus()) ? DELETED : ACTIVE);

                        destn.setPrice(productPrice);
                    } else {
                        destn.getProcessingReport().addEvent(WARN_PRODUCT_PRICE_DATA_NOT_AVAILABLE);
                    }
                }
            }
        }
    }

}
