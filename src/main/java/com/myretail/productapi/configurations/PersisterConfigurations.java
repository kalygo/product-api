package com.myretail.productapi.configurations;

import com.datastax.driver.mapping.Mapper;
import com.myretail.productapi.framework.persisters.ProductPricePersister;
import com.myretail.productapi.models.ProductPriceByTcin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PersisterConfigurations {

    @Autowired
    @Qualifier("productPriceByTcinDatastaxMapper")
    private Mapper<ProductPriceByTcin> productPriceByTcinDatastaxMapper;

    @Autowired
    private ProductPricePersister.DefaultConvertor productPricePersisterDefaultConvertor ;

    @Bean("product-price-persister")
    public ProductPricePersister productPricePersister(){
        return new ProductPricePersister(productPriceByTcinDatastaxMapper, productPricePersisterDefaultConvertor);
    }

    @Bean
    public ProductPricePersister.DefaultConvertor productPricePersisterDefaultConvertor(){
        return new ProductPricePersister.DefaultConvertor();
    }
}
