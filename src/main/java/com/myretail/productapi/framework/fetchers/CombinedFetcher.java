package com.myretail.productapi.framework.fetchers;

import com.myretail.productapi.framework.exceptions.XRuntimeException;

import java.util.Map;
import java.util.Set;


public interface CombinedFetcher<Id, Frr>{

    int parallelism();

    Set<Fetcher<Id, Frr>> fetchers();

    Map<Fetcher<Id, Frr>, Map<Id, Frr>> fetch(Set<Id> ids) throws XRuntimeException;
}
