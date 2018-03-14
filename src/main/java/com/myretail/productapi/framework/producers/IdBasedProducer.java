package com.myretail.productapi.framework.producers;

import com.myretail.productapi.framework.assembler.Assembler;
import com.myretail.productapi.framework.convertors.Convertor;
import com.myretail.productapi.framework.exceptions.XRuntimeException;
import com.myretail.productapi.framework.fetchers.CombinedFetcher;
import com.myretail.productapi.framework.fetchers.Fetcher;
import com.myretail.productapi.framework.validators.Validator;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class IdBasedProducer<Id, P, C, Frr> implements Producer<C> {

    private Set<Id> ids;

    private CombinedFetcher<Id, Frr> fetcher;

    private Function<Id, P> newInstanceSupplier;

    private Assembler<Id, P, Frr> assembler;

    private Convertor<P, C> convertor;

    public IdBasedProducer(Set<Id> ids, CombinedFetcher<Id, Frr> fetcher, Function<Id, P> newInstanceSupplier, Assembler<Id, P, Frr> assembler, Convertor<P, C> convertor) {
        this.ids = ids;
        this.fetcher = fetcher;
        this.newInstanceSupplier = newInstanceSupplier;
        this.assembler = assembler;
        this.convertor = convertor;
    }

    private Map<Fetcher<Id, Frr>, Map<Id, Frr>> fetch(Set<Id> ids) {
        return fetcher.fetch(ids);
    }

    public void assemble(Map<Id, P> byId, Map<Fetcher<Id, Frr>, Map<Id, Frr>> fetcherData) {
        assembler.assemble(byId, fetcherData);
    }

    public C convert(P p){
        return convertor.convert(p);
    }

    public Iterable<C> produce() throws XRuntimeException {

        Map<Id, P> newById = ids.stream().collect(toMap(id -> id, id -> newInstanceSupplier.apply(id)));

        assemble(newById, fetch(ids));

        return newById.values().parallelStream().map(p -> convert(p)).collect(toList());
    }

}
