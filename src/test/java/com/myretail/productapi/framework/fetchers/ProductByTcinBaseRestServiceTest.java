package com.myretail.productapi.framework.fetchers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.myretail.productapi.framework.service.ProductByTcinRestService;
import com.myretail.productapi.models.ProductByTcin;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductByTcinBaseRestServiceTest {

    private ProductByTcinRestService serviceUnderTest;

    private RestTemplate restTemplate;
    private Cache<Long, ProductByTcin> productByTcinCache;


    @Test
    public void test_getProductsByTcin_shouldReturnResponse() throws Exception {
        List<Long> ids = Lists.newArrayList(123L);

        restTemplate = mock(RestTemplate.class);
        productByTcinCache = CacheBuilder.newBuilder().build();
        serviceUnderTest = new ProductByTcinRestService(restTemplate, productByTcinCache);
        ResponseEntity<ProductByTcin[]> responseEntity = mock(ResponseEntity.class);
        ProductByTcin[] expected = {new ProductByTcin(123l)};

        when(responseEntity.getBody()).thenReturn(expected);
        when(restTemplate.getForEntity(anyString(), eq(ProductByTcin[].class), any(Map.class))).thenReturn(responseEntity);

        assertEquals(Lists.newArrayList(expected), serviceUnderTest.getProductsByTcin(ids, false));
    }

}