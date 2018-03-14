package com.myretail.productapi.framework.persisters;


public interface Persister<P, R> {
    EntityConvertor getEntityConvertor();

    R persist(P p);

    interface EntityConvertor<T, S>{
        T convert(S source);
    }
}
