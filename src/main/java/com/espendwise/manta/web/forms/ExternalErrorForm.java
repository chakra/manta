package com.espendwise.manta.web.forms;


import com.espendwise.manta.util.alert.ArgumentedMessage;
import com.espendwise.manta.util.alert.DisplayMessage;

import java.util.List;

public class ExternalErrorForm extends WebForm {

    private DisplayMessage message;
    private List<? extends ArgumentedMessage> reasons;
    private Throwable exc;
    private boolean resolved;

    public void setExc(Throwable exc) {
        this.exc = exc;
    }

    public void setReasons(List<? extends ArgumentedMessage> reasons) {
        this.reasons = reasons;
    }

    public void setMessage(DisplayMessage message) {
        this.message = message;
    }

    public DisplayMessage getMessage() {
        return message;
    }

    public List<? extends ArgumentedMessage> getReasons() {
        return reasons;
    }

    public Throwable getExc() {
        return exc;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isResolved() {
        return resolved;
    }
    public boolean getResolved() {
        return isResolved();
    }

    @Override
    public String toString() {
        return "ExternalErrorForm{" +
                "message=" + message +
                ", reasons=" + reasons +
                ", exc=" + exc +
                ", resolved=" + resolved +
                '}';
    }
}
