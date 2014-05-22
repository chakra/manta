package com.espendwise.manta.web.forms;


import java.util.List;

import com.espendwise.manta.model.view.CorporateScheduleView;

public class CorporateScheduleFilterResultForm  extends AbstractFilterResult<CorporateScheduleView> {

    private List<CorporateScheduleView> corporateSchedules;

    public List<CorporateScheduleView> getCorporateSchedules() {
        return corporateSchedules;
    }

    public void setCorporateSchedules(List<CorporateScheduleView> corporateSchedules) {
        this.corporateSchedules = corporateSchedules;
    }

    @Override
    public List<CorporateScheduleView> getResult() {
        return corporateSchedules;
    }

    @Override
    public void reset() {
        super.reset();
        corporateSchedules = null;
    }
}
