package com.espendwise.manta.web.forms;


import com.espendwise.manta.model.view.ManufacturerListView;

import java.util.List;

public class ManufacturerFilterResultForm  extends AbstractFilterResult<ManufacturerListView> {

    private List<ManufacturerListView> manufacturers;

    public ManufacturerFilterResultForm() {
    }

    public List<ManufacturerListView> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(List<ManufacturerListView> manufacturers) {
        this.manufacturers = manufacturers;
    }

    @Override
    public List<ManufacturerListView> getResult() {
        return manufacturers;
    }

    @Override
    public void reset() {
        super.reset();
        this.manufacturers = null;
    }


}
