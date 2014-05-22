package com.espendwise.manta.spi;


public interface Resolver<T, R> {

    public R resolve(T code) throws RuntimeException;

}
