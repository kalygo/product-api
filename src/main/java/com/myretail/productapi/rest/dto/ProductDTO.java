package com.myretail.productapi.rest.dto;

import com.myretail.productapi.framework.domain.entities.Product;

public class ProductDTO extends BaseDTO{
    private String tcin;
    private String title;
    private PriceDTO currentPrice;

    public ProductDTO(){

    }

    public ProductDTO(Long tcin, ErrorsDTO errors){
        this.tcin = String.valueOf(tcin);
        this.addErrors(errors.getErrors());
    }
    public String getTcin() {
        return tcin;
    }

    public void setTcin(Long tcin) {
        this.tcin =  String.valueOf(tcin);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public PriceDTO getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(PriceDTO currentPrice) {
        this.currentPrice = currentPrice;
    }
}
