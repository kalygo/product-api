package com.myretail.productapi.framework.validators;

public interface Validator<P> {
    boolean validate(P p);
}
