package com.myretail.productapi.framework.updater;

public interface Updater<Id, C, P, E> {
    boolean validate(Id id, C c);
    P convert(Id id, C c);
    void persist(P p);
    E update();

}
