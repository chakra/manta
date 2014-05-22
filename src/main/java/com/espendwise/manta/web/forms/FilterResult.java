package com.espendwise.manta.web.forms;


import com.espendwise.manta.web.util.SortHistory;

import java.util.List;

public interface FilterResult<T> {

    public List<T> getResult();

    public void setSortHistory(SortHistory history);

    public SortHistory getSortHistory();

}
