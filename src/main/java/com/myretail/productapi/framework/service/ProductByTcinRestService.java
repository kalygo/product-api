package com.myretail.productapi.framework.service;

import com.google.common.base.Joiner;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.myretail.productapi.models.ProductByTcin;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.StreamSupport.stream;

public class ProductByTcinRestService {

    @Autowired
    Cache<Long, ProductByTcin> productByTcinCache;

    private RestTemplate restTemplate;
    private String URL = "http://redsky.target.com/v2/pdp/tcin/{tcins}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

    public ProductByTcinRestService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public Iterable<ProductByTcin> getProductsByTcin(@NotNull Iterable<Long> tcins, boolean useCachedRecords) {
        if(!useCachedRecords){
            return getProductsByTcin(tcins);
        }

        Map<Long, ProductByTcin> availableFromCache = productByTcinCache.getAllPresent(tcins);
        Iterable<ProductByTcin> availableFromRemote = getProductsByTcin(stream(tcins.spliterator(), true).filter(t -> !availableFromCache.containsKey(t)).collect(toSet()));

        List<ProductByTcin> result = Lists.newLinkedList(availableFromRemote);
        result.addAll(availableFromCache.values());

        return result;
    }

    @HystrixCommand(fallbackMethod = "defaultEmptyProductByTcin")
    public Iterable<ProductByTcin> getProductsByTcin(@NotNull Iterable<Long> tcins) {
        if(!tcins.iterator().hasNext()) {
            return Lists.newLinkedList();

        }

        Map<String, String> uriVariables = new LinkedHashMap<>();
        uriVariables.put("tcins", Joiner.on(",").join(tcins));

        ProductByTcin[] productByTcins = restTemplate.getForEntity("http://localhost:8080/products/test/{tcins}", ProductByTcin[].class, uriVariables).getBody();

        writeToCache(productByTcins);

        return Lists.newArrayList(productByTcins);
    }

    private void writeToCache(ProductByTcin[] productByTcins) {
        Arrays.stream(productByTcins).forEach(entry -> productByTcinCache.put(entry.getTcin(), entry));
    }

    Iterable<ProductByTcin>  defaultEmptyProductByTcin(@NotNull Iterable<Long> tcins) {
        return Lists.newLinkedList();
        //return stream(tcins.spliterator(),true).map(t -> new ProductByTcin(t)).collect(toList());
    }
}