package com.myretail.productapi.framework.assembler;

import com.myretail.productapi.framework.fetchers.Fetcher;

import java.util.Map;

public class IdBasedAssembler<Id, P, Frr> implements Assembler<Id, P, Frr>{

    public void assemble(Map<Id, P> byId, Map<Fetcher<Id, Frr>, Map<Id, Frr>> fetcherData) {
        for (Fetcher fetcher : fetcherData.keySet()) {
            fetcher.getEntityMapper().map(byId, fetcherData.get(fetcher));
        }
    }
}