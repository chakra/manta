package com.espendwise.manta.spi;


import com.espendwise.manta.spi.Resolver;

public interface MessageResolver<T> extends Resolver<T, String> {

    public String resolve(T obj);

}
