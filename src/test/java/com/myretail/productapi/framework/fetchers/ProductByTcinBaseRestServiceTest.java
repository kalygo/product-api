package com.myretail.productapi.framework.fetchers;

import com.google.common.collect.Lists;
import com.myretail.productapi.framework.service.ProductByTcinRestService;
import com.myretail.productapi.models.ProductByTcin;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductByTcinBaseRestServiceTest {

    private ProductByTcinRestService serviceUnderTest;

    private RestTemplate restTemplate;

    @Test
    public void test_getProductsByTcin_shouldReturnResponse() throws Exception {
        restTemplate = mock(RestTemplate.class);
        serviceUnderTest = new ProductByTcinRestService(restTemplate);
        ResponseEntity<ProductByTcin[]> responseEntity = mock(ResponseEntity.class);
        ProductByTcin[] expected = new ProductByTcin[1];

        when(responseEntity.getBody()).thenReturn(expected);
        when(restTemplate.getForEntity(anyString(), eq(ProductByTcin[].class), any(Map.class))).thenReturn(responseEntity);

        assertEquals(Lists.newArrayList(expected), serviceUnderTest.getProductsByTcin(Lists.newArrayList(123L)));
    }

}