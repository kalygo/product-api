package com.myretail.productapi.configurations;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.myretail.productapi.models.ProductByTcin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfigurations {

    @Bean
    public Cache<Long, ProductByTcin> productByTcinCache() {
        return CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).concurrencyLevel(4).maximumSize(10000).build();
    }
}
