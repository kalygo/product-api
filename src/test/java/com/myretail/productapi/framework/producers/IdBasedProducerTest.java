package com.myretail.productapi.framework.producers;

import com.myretail.productapi.framework.assembler.Assembler;
import com.myretail.productapi.framework.convertors.Convertor;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.fetchers.CombinedFetcher;
import com.myretail.productapi.framework.validators.Validator;
import org.assertj.core.util.Sets;
import org.junit.Test;

import java.util.Set;
import java.util.function.Function;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class IdBasedProducerTest {
    private IdBasedProducer producerUnderTest;

    @Test
    public void test_prodcuer_shouldCallFetch_shouldCallAssemble_shouldCallValidate() throws Exception {
        Set<Long> testIds = Sets.newLinkedHashSet(123l);

        Function mockINstanceSupplier = mock(Function.class);
        when(mockINstanceSupplier.apply(123l)).thenReturn(new Product(123L));

        CombinedFetcher mockFetcher = mock(CombinedFetcher.class);
        Assembler mockAssembler = mock(Assembler.class);
        Validator mockValidator = mock(Validator.class);
        Convertor mockConvertor = mock(Convertor.class);

        producerUnderTest = new IdBasedProducer(testIds, mockFetcher, mockINstanceSupplier, mockAssembler, mockConvertor);
        producerUnderTest.produce();

        verify(mockINstanceSupplier, times(1)).apply(123l);
        verify(mockFetcher, times(1)).fetch(eq(testIds));

    }

    @Test
    public void validate() throws Exception {
    }

    @Test
    public void produce() throws Exception {
    }

}