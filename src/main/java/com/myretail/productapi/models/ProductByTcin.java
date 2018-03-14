package com.myretail.productapi.models;


import com.myretail.productapi.framework.fetchers.FetchedResultRow;
import com.myretail.productapi.framework.persisters.Persistable;

public class ProductByTcin implements FetchedResultRow, Persistable{

    private Long tcin;
    private String name;

    public ProductByTcin(Long tcin){
        this.tcin = tcin;
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
