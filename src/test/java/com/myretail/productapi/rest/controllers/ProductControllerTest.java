package com.myretail.productapi.rest.controllers;

import com.myretail.productapi.framework.fetchers.Fetcher;
import com.myretail.productapi.framework.fetchers.ProductByTcinFetcher;
import com.myretail.productapi.framework.fetchers.ProductPriceByTcinFetcher;
import org.junit.Test;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductControllerTest {
    private ProductController controllerUnderTest;

    private Map<String, Fetcher> fetchersByName;

    @Test
    public void getProduct() throws Exception {
        fetchersByName = mock(Map.class);
        ProductByTcinFetcher productByTcinFetcher = mock(ProductByTcinFetcher.class);
        ProductPriceByTcinFetcher productPriceByTcinFetcher = mock(ProductPriceByTcinFetcher.class);

        when(fetchersByName.get("product-fetcher")).thenReturn(productByTcinFetcher);
        when(fetchersByName.get("product-price-fetcher")).thenReturn(productPriceByTcinFetcher);

        controllerUnderTest.getProduct("123,223", "product-fetcher,product-price-fetcher");


    }

}