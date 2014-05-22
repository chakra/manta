package com.espendwise.manta.util.arguments;


import java.io.Serializable;

public interface Argument<T> extends Serializable {
    public T get();
}
