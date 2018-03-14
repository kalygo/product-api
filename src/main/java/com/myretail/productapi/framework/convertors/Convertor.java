package com.myretail.productapi.framework.convertors;

public interface Convertor<S, C> {
    C convert(S source);
}
