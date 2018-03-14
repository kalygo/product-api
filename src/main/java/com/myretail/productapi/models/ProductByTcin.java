package com.myretail.productapi.models;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.myretail.productapi.framework.domain.entities.Product;
import com.myretail.productapi.framework.fetchers.FetchedResultRow;
import com.myretail.productapi.framework.persisters.Persistable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductByTcin implements FetchedResultRow, Persistable{

    private Long tcin;
    private String name;

    public ProductByTcin(RestProduct restProduct){
        this.tcin = restProduct!=null && restProduct.getProduct()!=null && restProduct.getProduct().getItem()!=null
                && restProduct.getProduct().getItem().getTcin()!=null ?
                Long.valueOf(restProduct.getProduct().getItem().getTcin()) : null;

        this.name = restProduct!=null && restProduct.getProduct()!=null && restProduct.getProduct().getItem()!=null
                && restProduct.getProduct().getItem().getProductDescription()!=null ?
                restProduct.getProduct().getItem().getProductDescription().getTitle() : null;
    }

    public ProductByTcin(Long tcin){

    }

    public ProductByTcin() {

    }

    public Long getTcin() {
        return tcin;
    }

    public void setTcin(Long tcin) {
        this.tcin = tcin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
