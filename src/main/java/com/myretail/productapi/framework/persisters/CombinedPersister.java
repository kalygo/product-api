package com.myretail.productapi.framework.persisters;

import com.myretail.productapi.framework.exceptions.XRuntimeException;

import java.util.Map;
import java.util.Set;

public interface CombinedPersister<Id, P, Rst> {
    int parallelism();

    Set<Persister<P, Rst>> persisters();

    Map<Persister<P, Rst>, Rst> persist(P p) throws XRuntimeException;

}
