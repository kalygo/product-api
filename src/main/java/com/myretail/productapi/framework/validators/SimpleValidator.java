package com.myretail.productapi.framework.validators;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpleValidator<P> implements Validator<P>{

    private List<Predicate<P>> tests;

    public SimpleValidator(List<Predicate<P>> tests){
        this.tests= tests;
    }

    @Override
    public boolean validate(P p) {
        return ! tests.parallelStream().map(t -> t.test(p)).collect(Collectors.toSet()).contains(false);
    }
}
