package com.myretail.productapi.framework.domain.entities;

public enum CurrencyType {

    USD("United States Dollar", "USD");

    private String name;
    private String code;

    CurrencyType(String name, String code){
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
