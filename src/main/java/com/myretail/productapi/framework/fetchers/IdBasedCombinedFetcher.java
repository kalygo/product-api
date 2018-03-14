package com.myretail.productapi.framework.fetchers;


import com.google.common.collect.Maps;
import com.myretail.productapi.framework.exceptions.XRuntimeException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toMap;

public class IdBasedCombinedFetcher<Id, Frr> implements CombinedFetcher<Id, Frr> {

    private Set<Fetcher<Id, Frr>> fetchers;
    private int threadCount;

    public IdBasedCombinedFetcher(Set<Fetcher<Id, Frr>> fetchers, int parallelism){
        this.fetchers = fetchers;
        this.threadCount = parallelism;
    }

    @Override
    public Map<Fetcher<Id, Frr>, Map<Id, Frr>> fetch(Set<Id> ids) throws XRuntimeException {
        Map<Fetcher<Id, Frr>, Map<Id, Frr>> fetcherData = Maps.newLinkedHashMap();

        ExecutorService fetchExecutor = Executors.newFixedThreadPool(parallelism());

        Map<Fetcher, Future<Map<Id, Frr>>> futures = fetchers().parallelStream()
                .collect(toMap(f -> f,
                        f -> fetchExecutor.submit((Callable<Map<Id, Frr>>) () -> f.fetchData(ids))));

        try {
            for (Fetcher fetcher : fetchers()) {
                fetcherData.put(fetcher, futures.get(fetcher).get());
            }

        }catch (ExecutionException ee){
            throw new XRuntimeException(ee);
        }catch (InterruptedException ie){
            throw new XRuntimeException(ie);
        }

        return fetcherData;
    }

    @Override
    public int parallelism() {
        return threadCount;
    }

    @Override
    public Set<Fetcher<Id, Frr>> fetchers() {
        return fetchers;
    }
}
