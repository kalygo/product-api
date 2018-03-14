package com.myretail.productapi.configurations;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.myretail.productapi.framework.fetchers.ProductByTcinFetcher;
import com.myretail.productapi.framework.fetchers.ProductPriceByTcinFetcher;
import com.myretail.productapi.framework.service.ProductByTcinRestService;
import com.myretail.productapi.models.ProductByTcin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import static com.myretail.productapi.configurations.CassandraConfigurations.ProductPriceByTcinAccessor;

@Configuration
public class FetcherConfigurations {
    @Autowired
    private Cache<Long, ProductByTcin> productByTcinCache;

    @Autowired
    private ProductByTcinFetcher.DefaultMapper productByTcinFetcherDefaultMapper;
    @Autowired
    private ProductPriceByTcinFetcher.DefaultMapper productPriceByTcinFetcherDefaultMapper;
    @Autowired
    private ProductByTcinRestService productByTcinRestService;
    @Autowired
    private ProductPriceByTcinAccessor productPriceByTcinAccessor;

    @Bean(name = "product-fetcher")
    public ProductByTcinFetcher productByTcinFetcher(){
        return new ProductByTcinFetcher(productByTcinRestService, productByTcinFetcherDefaultMapper);
    }

    @Bean(name = "product-price-fetcher")
    public ProductPriceByTcinFetcher productPriceByTcinFetcher(){
        return new ProductPriceByTcinFetcher(productPriceByTcinAccessor, productPriceByTcinFetcherDefaultMapper);
    }

    @Bean
    public Cache<Long, ProductByTcin> productByTcinCache() {
        return CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).concurrencyLevel(4).maximumSize(10000).build();
    }

    @Bean
    public ProductByTcinRestService productByTcinRestService(RestTemplateBuilder builder){
        return new ProductByTcinRestService(builder.build(), productByTcinCache);
    }

    @Bean
    public ProductByTcinFetcher.DefaultMapper productByTcinFetcherDefaultMapper(){
        return new ProductByTcinFetcher.DefaultMapper();
    }
    @Bean
    public ProductPriceByTcinFetcher.DefaultMapper productPriceByTcinFetcherDefaultMapper(){
        return new ProductPriceByTcinFetcher.DefaultMapper();
    }
}
