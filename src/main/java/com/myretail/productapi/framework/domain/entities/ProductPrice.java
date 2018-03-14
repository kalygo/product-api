package com.myretail.productapi.framework.domain.entities;


import java.math.BigDecimal;

public class ProductPrice extends BaseEntity {
    private BigDecimal value;
    private CurrencyType currencyType;

    public ProductPrice(BigDecimal value, CurrencyType currencyType){
        this.value = value;
        this.currencyType = currencyType;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }
}
