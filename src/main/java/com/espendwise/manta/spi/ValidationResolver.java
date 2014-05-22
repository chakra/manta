package com.espendwise.manta.spi;


import com.espendwise.manta.util.alert.ArgumentedMessage;

import java.util.List;

public interface ValidationResolver<T> extends Resolver<T, List<? extends ArgumentedMessage>> {
    @Override
    List<? extends ArgumentedMessage> resolve(T code) throws RuntimeException;
}
