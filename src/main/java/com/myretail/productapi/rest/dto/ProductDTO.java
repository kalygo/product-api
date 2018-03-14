package com.myretail.productapi.rest.dto;

public class ProductDTO extends BaseDTO{
    private Long tcin;
    private String title;
    private PriceDTO currentPrice;

    public ProductDTO(){

    }

    public ProductDTO(Long tcin, ErrorsDTO errors){
        this.tcin = tcin;
        this.addErrors(errors.getErrors());
    }
    public Long getTcin() {
        return tcin;
    }

    public void setTcin(Long tcin) {
        this.tcin =  tcin;
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
