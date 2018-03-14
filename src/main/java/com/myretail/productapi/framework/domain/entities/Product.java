package com.myretail.productapi.framework.domain.entities;


public class Product {

    private Long tcin;
    private String name;
    private ProductPrice price;

    private ProcessingReport processingReport = new ProcessingReport();

    public Product(){
    }

    public Product(Long tcin){
        this.tcin = tcin;
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

    public ProductPrice getPrice() {
        return price;
    }

    public void setPrice(ProductPrice price) {
        this.price = price;
    }

    public ProcessingReport getProcessingReport() {
        return processingReport;
    }
}
