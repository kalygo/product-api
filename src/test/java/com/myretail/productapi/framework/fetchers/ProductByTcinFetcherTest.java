package com.myretail.productapi.framework.fetchers;

import com.google.common.collect.Lists;
import com.myretail.productapi.framework.service.ProductByTcinRestService;
import com.myretail.productapi.models.ProductByTcin;
import org.junit.Test;

import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductByTcinFetcherTest {

    private ProductByTcinFetcher fetcherUnderTest;
    private ProductByTcinRestService productByTcinRestService;
    private ProductByTcinFetcher.DefaultMapper productByTcinToProductMapper;

    @Test
    public void fetchData_withExistingTcin_shouldReturnProduct() throws Exception {
        productByTcinRestService = mock(ProductByTcinRestService.class);
        fetcherUnderTest = new ProductByTcinFetcher(productByTcinRestService, productByTcinToProductMapper);

        ProductByTcin p123 = new ProductByTcin(123l);

        when(productByTcinRestService.getProductsByTcin(newArrayList(123L))).thenReturn(newArrayList(p123));

        Map<Long, ProductByTcin> actual = fetcherUnderTest.fetchData(newArrayList(123L));

        assertEquals(1, actual.size());
        assertEquals(123L, actual.keySet().iterator().next().longValue());
        assertEquals(p123, actual.get(123L));
    }

    @Test
    public void fetchData_withInvalidTcin_shouldReturnEmptyProductWithId() throws Exception {
        productByTcinRestService = mock(ProductByTcinRestService.class);
        fetcherUnderTest = new ProductByTcinFetcher(productByTcinRestService, productByTcinToProductMapper);

        ProductByTcin p123 = new ProductByTcin(123l);
        when(productByTcinRestService.getProductsByTcin(newArrayList(123L))).thenReturn(Lists.newArrayList(p123));

        Map<Long, ProductByTcin> actual = fetcherUnderTest.fetchData(newArrayList(123L));

        assertEquals(1, actual.size());
        assertEquals(123L, actual.keySet().iterator().next().longValue());
        assertEquals(p123, actual.get(123L));

    }
}