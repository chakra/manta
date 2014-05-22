package com.espendwise.manta.web.forms;


import java.util.List;

public class FilterResultWebForm<T> extends AbstractFilterResult<T> {

    List<T> result;

    public FilterResultWebForm(List<T> result) {
        this.result = result;
    }

    @Override
    public List<T> getResult() {
        return result;
    }

    @Override
    public void reset() {
        super.reset();
        this.result = null;
    }

}
