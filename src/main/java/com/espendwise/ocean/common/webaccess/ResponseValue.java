package com.espendwise.ocean.common.webaccess;

import java.util.List;

public interface ResponseValue<T> {
    public T getObject();
    public int getStatus();
    public List<String> getErrors();
}
