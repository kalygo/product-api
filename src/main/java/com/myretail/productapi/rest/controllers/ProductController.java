package com.myretail.productapi.rest.controllers;

import com.google.common.base.Splitter;
import com.google.common.collect.Sets;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.exceptions.XRuntimeException;
import com.myretail.productapi.framework.fetchers.FetchedResultRow;
import com.myretail.productapi.framework.fetchers.Fetcher;
import com.myretail.productapi.framework.persisters.Persister;
import com.myretail.productapi.framework.producers.ProducerFactory;
import com.myretail.productapi.framework.updater.ProductUpdater;
import com.myretail.productapi.framework.updater.UpdaterFactory;
import com.myretail.productapi.models.ProductByTcin;
import com.myretail.productapi.rest.dto.ErrorsDTO;
import com.myretail.productapi.rest.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang.math.NumberUtils.createLong;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private Map<String, Fetcher> fetchersByName;

    @Autowired
    private Map<String, Persister> persistersByName;

    @Autowired
    private ProducerFactory producerFactory;

    @RequestMapping(value = "/{tcins}", method = GET)
    @ResponseBody
    public Iterable<ProductDTO> getProduct(@PathVariable(value = "tcins") String tcins, @RequestParam(value = "includes", required = false, defaultValue = "product-fetcher") String fetcherNames) throws XRuntimeException {


        Set<Fetcher<Long, FetchedResultRow>> fetcherSet = Splitter.on(',').trimResults().omitEmptyStrings()
                .splitToList(fetcherNames).parallelStream()
                .map(name -> (Fetcher<Long, FetchedResultRow>) fetchersByName.get(name))
                .collect(toSet());

        Set<Long> tcinList = Splitter.on(',').trimResults().omitEmptyStrings()
                .splitToList(tcins).parallelStream()
                .map(s -> createLong(s))
                .collect(toSet());

        return producerFactory.newProductProducer(tcinList, fetcherSet).produce();
    }


    @RequestMapping(value = "/{tcin}", method = PUT)
    public ResponseEntity updateProduct(@PathVariable(value = "tcin") Long tcin, @RequestBody ProductDTO productDTO){

        ProductUpdater updater = UpdaterFactory.newProductUpdater(tcin, productDTO, productPersisters());

        ErrorsDTO errorsOnUpdate = updater.update();
        if(errorsOnUpdate.hasErrors()) {
            return ResponseEntity.badRequest().body(new ProductDTO(tcin, errorsOnUpdate));
        } else {
            return ResponseEntity.ok().build();
        }
    }


    private Set<Persister<Product, Boolean>> productPersisters(){
        Set<String> productPersisters = Sets.newHashSet("product-price-persister");

        return productPersisters.stream().map(pp -> (Persister<Product, Boolean>) persistersByName.get(pp)).collect(Collectors.toSet());
    }



    @RequestMapping("/test/{tcins}")
    public ProductByTcin[] getProductsByTcins(@PathVariable(value = "tcins") String tcins){

        List<ProductByTcin> collect = Splitter.on(',').trimResults().omitEmptyStrings()
                .splitToList(tcins).parallelStream()
                .map(s -> createDummyProduct(createLong(s)))
                .collect(Collectors.toList());
        //collect.remove(0);
        return collect.stream().toArray(size -> new ProductByTcin[size]);

    }

    private ProductByTcin createDummyProduct(Long tcin) {
        ProductByTcin productByTcin1 = new ProductByTcin();
        productByTcin1.setTcin(tcin);
        productByTcin1.setName("Title_"+tcin);
        return productByTcin1;
    }
}
