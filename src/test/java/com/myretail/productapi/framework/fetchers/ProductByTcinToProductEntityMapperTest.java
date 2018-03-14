package com.myretail.productapi.framework.fetchers;

import com.google.common.collect.Maps;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.models.ProductByTcin;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ProductByTcinToProductEntityMapperTest {
    private ProductByTcinFetcher.DefaultMapper mapperUnderTest;

    @Test
    public void test_map_method_shouldMapSuccessfully() throws Exception {
        mapperUnderTest = new ProductByTcinFetcher.DefaultMapper();
        Map<Long, Product> destn = Maps.newLinkedHashMap();
        destn.put(123L, new Product(123L));

        Map<Long, ProductByTcin> source = new HashMap<>();
        ProductByTcin src123 = new ProductByTcin();
        src123.setTcin(123L);
        src123.setName("name_123");
        source.put(123L, src123);

        mapperUnderTest.map(destn, source);

        assertEquals(src123.getTcin(), destn.get(123L).getTcin());
        assertEquals(src123.getName(), destn.get(123L).getName());
    }

}