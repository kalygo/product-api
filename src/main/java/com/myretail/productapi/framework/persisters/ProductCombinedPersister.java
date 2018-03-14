package com.myretail.productapi.framework.persisters;

import com.google.common.collect.Maps;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.exceptions.XRuntimeException;
import com.myretail.productapi.framework.persisters.Persister.EntityConvertor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProductCombinedPersister implements CombinedPersister<Long, Product, Boolean> {

    private Set<Persister<Product, Boolean>> persisters;
    private int threadCount;

    @Override
    public int parallelism() {
        return threadCount;
    }

    public ProductCombinedPersister(Set<Persister<Product, Boolean>> persisters, int parallelism){
        this.persisters=persisters;
        this.threadCount=parallelism;
    }

    @Override
    public Set<Persister<Product, Boolean>> persisters() {
        return this.persisters;
    }

    @Override
    public Map<Persister<Product, Boolean>, Boolean> persist(Product product) throws XRuntimeException {


        ExecutorService persistExecutor = Executors.newFixedThreadPool(parallelism());
        Map<Persister, Future<Boolean>> futures = new HashMap<>();
        for(Persister persister : persisters){
            EntityConvertor<Persistable, Product> entityConvertor = persister.getEntityConvertor();
            Future future = persistExecutor.submit(() -> {
                Persistable persistable = entityConvertor.convert(product);
                return persister.persist(persistable);
            });
            futures.put(persister, future);

        }

        Map<Persister<Product, Boolean>, Boolean> resultMap = Maps.newLinkedHashMap();
        try {
            for (Persister persister : persisters()) {
                resultMap.put(persister, futures.get(persister).get());
            }

        }catch (ExecutionException ee){
            throw new XRuntimeException(ee);
        }catch (InterruptedException ie){
            throw new XRuntimeException(ie);
        }

        return resultMap;
    }
}
