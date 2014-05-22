package com.espendwise.manta.web.forms;


import java.util.List;

import com.espendwise.manta.history.HistoryRecord;

public class HistoryFilterResultForm  extends AbstractFilterResult<HistoryRecord> {

    private List<HistoryRecord> historyRecords;

    public HistoryFilterResultForm() {
    }

    public List<HistoryRecord> getHistoryRecords() {
        return historyRecords;
    }

    public void setHistoryRecords(List<HistoryRecord> historyRecords) {
        this.historyRecords = historyRecords;
    }

    @Override
    public List<HistoryRecord> getResult() {
        return historyRecords;
    }

	@Override
    public void reset() {
        super.reset();
        this.historyRecords = null;
    }

}
