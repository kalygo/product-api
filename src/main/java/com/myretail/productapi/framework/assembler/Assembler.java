package com.myretail.productapi.framework.assembler;

import com.myretail.productapi.framework.fetchers.Fetcher;

import java.util.Map;


public interface Assembler<Id, P, Frr> {

    void assemble(Map<Id, P> byId, Map<Fetcher<Id, Frr>, Map<Id, Frr>> fetcherData);
}
