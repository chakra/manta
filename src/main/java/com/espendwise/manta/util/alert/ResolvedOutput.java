package com.espendwise.manta.util.alert;


import java.io.Serializable;

public interface ResolvedOutput<T> extends Serializable {
    public String resolve();
}
