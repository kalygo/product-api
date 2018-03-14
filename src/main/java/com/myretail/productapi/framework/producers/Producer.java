package com.myretail.productapi.framework.producers;

public interface Producer<T> {

    Iterable<T> produce();
}
