package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.alert.ArgumentedMessage;

import java.util.List;

public interface ValidationResult {
    public List<? extends ArgumentedMessage> getResult();
}
