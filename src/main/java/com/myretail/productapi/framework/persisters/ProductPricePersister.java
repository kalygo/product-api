package com.myretail.productapi.framework.persisters;

import com.datastax.driver.mapping.Mapper;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.models.ProductPriceByTcin;
import com.myretail.productapi.models.Status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static com.myretail.productapi.models.Status.DELETED;

public class ProductPricePersister implements Persister<ProductPriceByTcin, Boolean> {

    private Mapper<ProductPriceByTcin> productPriceByTcinDatastaxMapper;
    private EntityConvertor entityConvertor;

    public ProductPricePersister(Mapper<ProductPriceByTcin> productPriceByTcinDatastaxMapper, EntityConvertor entityConvertor) {
        this.productPriceByTcinDatastaxMapper = productPriceByTcinDatastaxMapper;
        this.entityConvertor=entityConvertor;
    }

    @Override
    public EntityConvertor getEntityConvertor() {
        return entityConvertor;
    }

    @Override
    public Boolean persist(ProductPriceByTcin productPriceByTcin) {
        Map<Long, Boolean> resultMap = new HashMap<>();

        if (null != productPriceByTcin) {
            productPriceByTcinDatastaxMapper.save(productPriceByTcin);
            resultMap.put(productPriceByTcin.getTcin(), Boolean.TRUE);
        }
        return true;
    }

    public static class DefaultConvertor implements EntityConvertor<ProductPriceByTcin, Product> {
        @Override
        public ProductPriceByTcin convert(Product source) {
            if(source!=null && source.getPrice()!=null){
                ProductPriceByTcin price = new ProductPriceByTcin();
                price.setStatus(source.getPrice().isActive() ? Status.ACTIVE : DELETED);
                price.setCurrencyCode(source.getPrice().getCurrencyType().getCode());
                price.setPrice(new BigDecimal(source.getPrice().getValue().toString()));
                price.setTcin(source.getTcin());
                return price;
            }

            return null;
        }
    }
}