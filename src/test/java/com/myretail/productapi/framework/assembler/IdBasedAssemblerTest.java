package com.myretail.productapi.framework.assembler;

import com.myretail.productapi.framework.fetchers.Fetcher;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.Map;

public class IdBasedAssemblerTest {
    private IdBasedAssembler assemblerUnderTest;

    @Test
    public void test_assemble_withStringMapper_shouldMapSuccessfully() throws Exception {
        assemblerUnderTest = new IdBasedAssembler();

        Map<Long, StringBuilder> mappingOutputById = new LinkedHashMap<>();
        mappingOutputById.put(123L, new StringBuilder());

        Fetcher<Long, String> mockFetcher = Mockito.mock(Fetcher.class);
        Mockito.when(mockFetcher.getEntityMapper()).thenReturn(simpleStringMapper());


        Map<Long, String> map = new LinkedHashMap<>();
        map.put(123l, "one-two-three");

        Map<Fetcher<Long, String>, Map<Long, String>> fetcherData = new LinkedHashMap<>();
        fetcherData.put(mockFetcher, map);


        assemblerUnderTest.assemble(mappingOutputById, fetcherData);


        Assert.assertEquals("one-two-three", mappingOutputById.get(123l).toString());
    }

    private Fetcher.EntityMapper<Long, StringBuilder, String> simpleStringMapper(){
        return new Fetcher.EntityMapper<Long, StringBuilder, String>(){

            @Override
            public void map(Map<Long, StringBuilder> destinationById, Map<Long, String> sourceById) {
                for(Long key : sourceById.keySet()) {
                    destinationById.get(key).append(sourceById.get(key));
                }
            }
        };
    }

}