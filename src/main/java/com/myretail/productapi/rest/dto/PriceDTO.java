package com.myretail.productapi.rest.dto;

import com.myretail.productapi.framework.domain.entities.CurrencyType;

import java.math.BigDecimal;


public class PriceDTO extends BaseDTO{
    private BigDecimal value;
    private CurrencyType currency;
    private Boolean deleted;

    public Boolean isDeleted() {
        return deleted;
        //return deleted!=null && deleted.equals(Boolean.TRUE);
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyType currency) {
        this.currency = currency;
    }


}
