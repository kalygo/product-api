package com.myretail.productapi.framework.service;

import com.google.common.base.Joiner;
import com.google.common.cache.Cache;
import com.google.common.collect.Lists;
import com.myretail.productapi.models.ProductByTcin;
import com.myretail.productapi.models.RestProduct;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class ProductByTcinRestService {

    private Cache<Long, ProductByTcin> productByTcinCache;

    private RestTemplate restTemplate;

    private String url;

    public ProductByTcinRestService(RestTemplate restTemplate, Cache<Long, ProductByTcin> productByTcinCache, String url){
        this.restTemplate = restTemplate;
        this.productByTcinCache = productByTcinCache;
        this.url=url;
    }

    public Iterable<ProductByTcin> getProductsByTcin(@NotNull List<Long> tcins, boolean useCachedRecords) {
        if(!useCachedRecords){
            return getProductsByTcin(tcins);
        }

        Map<Long, ProductByTcin> availableFromCache = productByTcinCache.getAllPresent(tcins);
        Iterable<ProductByTcin> availableFromRemote = getProductsByTcin(stream(tcins.spliterator(), true).filter(t -> !availableFromCache.containsKey(t)).collect(toList()));
        
        return productByTcinCache.getAllPresent(tcins).values();
    }

    @HystrixCommand(fallbackMethod = "defaultEmptyProductByTcin")
    private Iterable<ProductByTcin> getProductsByTcin(@NotNull List<Long> tcins) {
        if(!tcins.iterator().hasNext()) {
            return Lists.newLinkedList();

        }

        Map<String, String> uriVariables = new LinkedHashMap<>();
        uriVariables.put("tcins", Joiner.on(",").join(tcins));


        List<ProductByTcin> productByTcins = new LinkedList<>();

        if(tcins.size() > 1) {
            ResponseEntity<RestProduct[]> responseEntity1 = restTemplate.getForEntity(url, RestProduct[].class, uriVariables);
            productByTcins = Arrays.stream(responseEntity1.getBody()).map(rp -> new ProductByTcin(rp)).collect(Collectors.toList());
        }
        if(tcins.size()==1) {
            ResponseEntity<RestProduct> responseEntity2 = restTemplate.getForEntity(url, RestProduct.class, uriVariables);
            productByTcins.add(new ProductByTcin(responseEntity2.getBody()));
        }

        writeToCache(productByTcins);

        return Lists.newArrayList(productByTcins);
    }

    private void writeToCache(List<ProductByTcin> productByTcins) {
        productByTcins.forEach(entry -> writeToCache(entry));
    }

    private void writeToCache(ProductByTcin productByTcin) {
        if(productByTcin.getTcin()!=null) {
            productByTcinCache.put(productByTcin.getTcin(), productByTcin);
        }
    }

    Iterable<ProductByTcin>  defaultEmptyProductByTcin(@NotNull Iterable<Long> tcins) {
        return Lists.newLinkedList();
    }
}