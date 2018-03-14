package com.myretail.productapi.framework.fetchers;

import javax.validation.constraints.NotNull;
import java.util.Map;


public interface Fetcher<Id, Frr>{

    EntityMapper getEntityMapper();

    Map<Id, Frr> fetchData(Iterable<Id> tcins);

    interface EntityMapper<Id, T, Result>{
        void map(@NotNull Map<Id, T> destinationById, Map<Id, Result> sourceById);

    }
}
