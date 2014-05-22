package com.espendwise.manta.util.validation;


import com.espendwise.manta.util.trace.ApplicationExceptionCode;
import com.espendwise.manta.util.trace.ApplicationReason;
import com.espendwise.manta.util.arguments.TypedArgument;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class ValidationRuleResult {

    private static final Logger logger = Logger.getLogger(ValidationRuleResult.class);

    public static final  String FAILED = "FAILED" ;
    public static final  String SUCCESS = "SUCCESS" ;

    private String status;
    private List<ApplicationExceptionCode> codes;

    public ValidationRuleResult() {
        this.codes = new ArrayList<ApplicationExceptionCode>();
    }

    public String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }

    public void success() {
        setStatus(SUCCESS);
    }
    public boolean  isFailed() {
        return FAILED.equals(getStatus());
    }

    public void failed(ApplicationReason reason) {
        setStatus(FAILED);
        codes.add(new ApplicationExceptionCode<ApplicationReason>(reason));
    }

    public void failed(ApplicationReason reason, TypedArgument... arguments) {
        setStatus(FAILED);
        codes.add(new ApplicationExceptionCode<ApplicationReason>(reason, arguments));
    }

    public List<ApplicationExceptionCode> getCodes() {
        return codes;
    }
}
