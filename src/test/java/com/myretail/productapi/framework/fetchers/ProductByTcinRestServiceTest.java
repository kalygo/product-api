package com.myretail.productapi.framework.fetchers;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.myretail.productapi.framework.service.ProductByTcinRestService;
import com.myretail.productapi.models.ProductByTcin;
import com.myretail.productapi.models.RestProduct;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ProductByTcinRestServiceTest {

    private ProductByTcinRestService serviceUnderTest;

    private RestTemplate restTemplate;
    private Cache<Long, ProductByTcin> productByTcinCache;


    @Test
    public void test_getProductsByTcin_shouldReturnResponse() throws Exception {
        List<Long> ids = Lists.newArrayList(123L);

        restTemplate = mock(RestTemplate.class);
        productByTcinCache = CacheBuilder.newBuilder().build();
        serviceUnderTest = new ProductByTcinRestService(restTemplate, productByTcinCache,"some_url");
        ResponseEntity<RestProduct> responseEntity = mock(ResponseEntity.class);
        RestProduct restProduct = getRestProduct();

        when(responseEntity.getBody()).thenReturn(restProduct);
        when(restTemplate.getForEntity(anyString(), eq(RestProduct.class), any(Map.class))).thenReturn(responseEntity);

        assertEquals(123l, serviceUnderTest.getProductsByTcin(ids, false).iterator().next().getTcin().longValue());
    }

    private RestProduct getRestProduct() {
        RestProduct result = new RestProduct();
        RestProduct.Product product = new RestProduct.Product();
        RestProduct.Product.Item item = new RestProduct.Product.Item();
        item.setTcin("123");
        product.setItem(item);
        result.setProduct(product);

        return result;
    }

}