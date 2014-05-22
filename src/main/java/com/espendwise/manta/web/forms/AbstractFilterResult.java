package com.espendwise.manta.web.forms;


import com.espendwise.manta.spi.Resetable;
import com.espendwise.manta.web.util.SortHistory;

import java.util.List;

public abstract class AbstractFilterResult<T> extends WebForm implements FilterResult<T>, Resetable {

    private SortHistory sortHistory;

    @Override
    public void setSortHistory(SortHistory history) {
        this.sortHistory = history;
    }

    @Override
    public SortHistory getSortHistory() {
        return this.sortHistory;
    }

    @Override
    public abstract List<T> getResult();

    public void reset() {
        sortHistory = null;
    }

}
