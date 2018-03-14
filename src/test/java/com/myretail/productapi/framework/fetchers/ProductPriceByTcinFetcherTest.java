package com.myretail.productapi.framework.fetchers;

import com.datastax.driver.mapping.Result;
import com.google.common.collect.Lists;
import com.myretail.productapi.configurations.CassandraConfigurations.ProductPriceByTcinAccessor;
import com.myretail.productapi.models.ProductPriceByTcin;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductPriceByTcinFetcherTest {

    private ProductPriceByTcinFetcher fetcherUnderTest;
    private ProductPriceByTcinAccessor productPriceByTcinAccessor;
    private ProductPriceByTcinFetcher.DefaultMapper productPriceByTcinToProductMapper;

    @Test
    public void fetchData_withExistingTcin_shouldReturnPrice() throws Exception {
        productPriceByTcinAccessor = mock(ProductPriceByTcinAccessor.class);
        fetcherUnderTest = new ProductPriceByTcinFetcher(productPriceByTcinAccessor, productPriceByTcinToProductMapper);

        Result<ProductPriceByTcin> r123 = mock(Result.class);
        ProductPriceByTcin p123 = new ProductPriceByTcin();

        when(r123.one()).thenReturn(p123);
        when(productPriceByTcinAccessor.getByTcin(123L)).thenReturn(r123);

        Map<Long, ProductPriceByTcin> actual = fetcherUnderTest.fetchData(Lists.newArrayList(123L));

        assertEquals(1, actual.size());
        assertEquals(123L, actual.keySet().iterator().next().longValue());
        assertEquals(p123, actual.get(123L));
    }

    @Test
    public void fetchData_withInvalidTcin_shouldReturnNothing() throws Exception {
        productPriceByTcinAccessor = mock(ProductPriceByTcinAccessor.class);
        fetcherUnderTest = new ProductPriceByTcinFetcher(productPriceByTcinAccessor, productPriceByTcinToProductMapper);

        Result<ProductPriceByTcin> r123 = mock(Result.class);

        when(r123.one()).thenReturn(null);
        when(productPriceByTcinAccessor.getByTcin(123L)).thenReturn(r123);

        Map<Long, ProductPriceByTcin> actual = fetcherUnderTest.fetchData(Lists.newArrayList(123L));

        assertTrue(actual.get(123L)==null);
        assertEquals(0, actual.size());
    }
}