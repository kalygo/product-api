package com.myretail.productapi.models;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.myretail.productapi.framework.fetchers.FetchedResultRow;
import com.myretail.productapi.framework.persisters.Persistable;

import java.math.BigDecimal;

@Table(name = "product_price_by_tcin", keyspace = "product_ks")
public class ProductPriceByTcin implements FetchedResultRow, Persistable {

    @PartitionKey
    @Column(name = "tcin")
    private Long tcin;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "status")
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getTcin() {
        return tcin;
    }

    public void setTcin(Long tcin) {
        this.tcin = tcin;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
